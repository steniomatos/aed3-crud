package br.pucminas.crud;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import hash.Hash;

public class Arquivo<T extends Registro>
{
	/**
	 * Nome do arquivo
	 */
	public String nomeArquivo;

	/**
	 * Objeto arquivo
	 */
	public RandomAccessFile arquivo;

	/**
	 * Handler da tabela hash dos IDs
	 */
	public Hash<Integer, Long> hashID;

	/**
	 * Construtor da classe genérica
	 */
	private Constructor<T> construtor;

	/**
	 * Tamanho em bytes do cabeçalho do arquivo
	 */
	private final int HEADER_SIZE = 4;
	
	/**
	 * Cria arquivo de dados para a entidade.
	 * @param _classe Classe da entidade
	 * @param _nomeArquivo Nome do arquivo
	 * @throws Exception
	 */
	public Arquivo(Class<T> _classe, String _nomeArquivo) throws Exception
	{
		construtor = _classe.getConstructor();
		nomeArquivo = _nomeArquivo;

		T obj = construtor.newInstance();

		File d = new File("dados"); // Diretório para o arquivo

		if(!d.exists())
			d.mkdir();

		arquivo = new RandomAccessFile("dados/" + nomeArquivo, "rw"); // Abrir o arquivo

		hashID = new Hash<>("dados/" + obj.getTableName() + "_idIndexDir.db", 
							"dados/" + obj.getTableName() + "_idIndexBuckets.db", 
							16,	  
							Integer.class, 
							Long.class );

		// Se o arquivo for menor do que o tamanho do cabeçalho, logo não possuir cabeçalho
		// Escreve 0 para representar o último ID utilizado
		if(arquivo.length() < HEADER_SIZE)
			arquivo.writeInt(0);        
	}
	
	/**
	 * Inclui um registro
	 * @param _obj Registro
	 * @return ID do um registro
	 * @throws Exception
	 */
	public int incluir(T _obj)
	{
		int id = -1;
		
		try
		{
			id = incluir(_obj, 0, arquivo.length());
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return id;
	}

	/**
	 * Inclui um registro
	 * @param _obj Registro
	 * @param _lixo Tamanho do lixo após o registro
	 * @param _pos Posição a ser escrita
	 * @return ID do um registro
	 */
	private int incluir(T _obj, int _lixo, long _pos)
	{
		try
		{
			if (_obj.getID() <= 0)
			{
				arquivo.seek(0);
				
				int ultimoID = arquivo.readInt() + 1;
				_obj.setID(ultimoID);
				
				arquivo.seek(0);
				arquivo.writeInt(ultimoID);

				inserirIndex(_obj.getID(), _pos);
			}
			else
				alterarIndex(_obj.getID(), _pos);
			
			arquivo.seek(_pos);

			arquivo.writeByte(' ');
			byte[] byteArray = _obj.toByteArray();

			arquivo.writeInt(byteArray.length); // Tamanho do registro
			arquivo.write(byteArray);

			arquivo.writeInt(_lixo);
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return _obj.getID();
	}
	
	// Método apenas para testes, pois geralmente a memória principal raramente
	// será suficiente para manter todos os registros simultaneamente
	/**
	 * Lista os registros do arquivo
	 * @return Array com os registros
	 * @throws Exception
	 */
	public ArrayList<T> listar() throws Exception
	{
		ArrayList<T> lista = new ArrayList<>();

		arquivo.seek(HEADER_SIZE);
		
		byte lapide;
		byte[] byteArray;
		int size;
		T obj;
		
		while(arquivo.getFilePointer() < arquivo.length())
		{
			obj = construtor.newInstance();
			lapide = arquivo.readByte();
			size = arquivo.readInt();
		
			byteArray = new byte[size];
		
			arquivo.read(byteArray);
		
			if(lapide == ' ')
			{
				obj.fromByteArray(byteArray);
				lista.add(obj);
			}

			arquivo.skipBytes(arquivo.readInt()); // Pular o lixo
		}
		
		return lista;
	}
	
	/**
	 * Encontra um registro
	 * @param _id ID do registro
	 * @return Objeto genérico com os dados do registro
	 */
	public T buscar(int _id)
	{
		try
		{
			arquivo.seek(HEADER_SIZE);

			byte lapide;
			byte[] byteArray;
			int size;
			T obj = null;

			long pos = getPosicao(_id);
			

			if (pos > 0)
			{
				arquivo.seek(pos);
				
				lapide = arquivo.readByte();

				if (lapide == ' ')
				{
					obj = construtor.newInstance();

					size = arquivo.readInt();

					byteArray = new byte[size];

					arquivo.read(byteArray);
					obj.fromByteArray(byteArray);

					return obj;
				}
			}
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Exclui um registro
	 * @param _id ID do registro
	 * @return Se excluiu
	 */
	public boolean excluir(int _id)
	{
		long endereco = getPosicao(_id);

		if (endereco < HEADER_SIZE)
			return false;

		try
		{
			arquivo.seek(endereco);
			arquivo.writeByte('*');
			excluirIndex(_id);
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Insere novo registro no arquivo de index
	 * @param _id ID do registro
	 * @param _pos Posição do registro
	 * @throws Exception
	 */
	public void inserirIndex(int _id, long _pos) throws Exception
	{
		hashID.inserir(_id, _pos);
	}

	/**
	 * Altera posição de um registro armazenada no index
	 * @param _id ID do registro
	 * @param _newPos Nova posição do registro
	 * @return Se conseguiu fazer a alteração
	 */
	public boolean alterarIndex(int _id, long _newPos)
	{
		if (_id < 1) return false;

		if (hashID.excluir(_id))
			return hashID.inserir(_id, _newPos);
		
		return false;
	}

	/**
	 * Exclui um índice da tabela hash
	 * @param _id id a ser excluído
	 * @return {@code true} caso dê tudo certo, {@code false} caso algo dê errado
	 */
	public boolean excluirIndex(int _id)
	{
		return hashID.excluir(_id);
	}

	/**
	 * Recupera a posição de um registro
	 * @param _id ID do registro
	 * @return Posição do registro
	 */
	public long getPosicao(int _id)
	{
		try
		{
			return hashID.pesquisarDadoComAChave(_id);
		}
		
		catch (Exception e)
		{
			return -1;
		}
	}

	/**
	 * Altera um registro
	 * @param _obj Dados do registro
	 * @return Se houve sucesso
	 */
	public boolean alterar(T _obj)
	{
		if (_obj.getID() <= 0)
			return false;

		long endereco = getPosicao(_obj.getID());

		if (endereco <= 0)
			return false;

		try
		{
			byte[] objData = _obj.toByteArray();

			arquivo.seek(endereco);

			arquivo.skipBytes(1);

			int size = arquivo.readInt(); // Tamanho do registro

			arquivo.skipBytes(size);
			size += arquivo.readInt(); // O tamanho disponível é o tamanho do registro, mais o lixo após o registro

			arquivo.seek(endereco);

			if (size >= objData.length)
				incluir(_obj, size - objData.length, endereco);
			else
			{
				arquivo.writeByte('*');
				incluir(_obj);
			}
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Fecha os arquivos abertos na instância
	 * @throws Exception
	 */
	public void fecha() throws Exception
	{
		arquivo.close();
		hashID.fechar();
	}

	/**
	 * Busca todas as entradas que obedecem uma chave
	 * @param chave Valor da chave
	 * @param skipBefore Função com a leitura dos bytes anteriores à localização da chave
	 * @return Array com os objetos encontrados
	 */
	public Object[] buscaPorChave(int chave, Runnable skipBefore)
	{
		ArrayList<T> lista = new ArrayList<>();

		long pos = 4;
		int chaveLida, idLido, size;
		byte lapide;

		try
		{
			arquivo.seek(pos);

			do
			{
				lapide = arquivo.readByte();
				size   = arquivo.readInt();

				pos = arquivo.getFilePointer();

				idLido = arquivo.readInt();
				
				skipBefore.run();
				
				chaveLida = arquivo.readInt();

				if (lapide == ' ' && chaveLida == chave)
					lista.add(buscar(idLido));
				

				arquivo.seek(pos + size);
				int sizeLixo = arquivo.readInt();
				arquivo.skipBytes(sizeLixo);

			} while(true);
		}
		
		catch (IOException e)
		{
			
		}

		return lista.size() <= 0 ? new Object[0] : lista.toArray();
	}

	/**
	 * Move o ponteiro do arquivo um dado número de bytes
	 * @param num Número de bytes a mover o ponteiro
	 * @throws IOException
	 */
	public void skipBytes(int num) throws IOException
	{
		this.arquivo.skipBytes(num);
	}

	/**
	 * Move o ponteiro do arquivo um {@code short} para frente
	 * @throws IOException
	 */
	public void skipShort() throws IOException
	{
		this.arquivo.readShort();
	}

	/**
	 * Move o ponteiro do arquivo um {@code int} para frente
	 * @throws IOException
	 */
	public void skipInt() throws IOException
	{
		this.arquivo.readInt();
	}

	/**
	 * Move o ponteiro do arquivo um {@code long} para frente
	 * @throws IOException
	 */
	public void skipLong() throws IOException
	{
		this.arquivo.readLong();
	}

	/**
	 * Move o ponteiro do arquivo um {@code char} para frente
	 * @throws IOException
	 */
	public void skipChar() throws IOException
	{
		this.arquivo.readChar();
	}

	/**
	 * Move o ponteiro do arquivo uma {@code String} de tamanho variável para frente
	 * @throws IOException
	 */
	public void skipString() throws IOException
	{
		short strSize = this.arquivo.readShort();
		skipBytes(strSize);
	}
}
