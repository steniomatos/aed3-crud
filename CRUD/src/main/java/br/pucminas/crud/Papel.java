package br.pucminas.crud;

import java.io.*;

/**
 * @author Stenio Matos <stenioduartecosta@gmail.com>
 */

public class Papel implements Registro
{
	private int id, idAtor, idFilme;
	private String nomePersonagem;
    private final String TABLE_NAME = "papel";

    public Papel()
    {
		this.id = 0;
		this.idAtor = 0;
		this.idFilme = 0;
		this.nomePersonagem = "";
	}
	
    public Papel(int _idAtor, int _idFilme, String _nomePersonagem)
    {
		this.id = 0;
		this.idAtor = _idAtor;
		this.idFilme = _idFilme;
		this.nomePersonagem = _nomePersonagem;
    }

//---Metodos get:

    @Override
	public int getID()
	{
		return this.id;
	}
	  
	public int getIdAtor()
	{
		return idAtor;
	}

	public int getIdFilme()
	{
		return idFilme;
	}

	public String getNomePersonagem()
	{
		return nomePersonagem;
	}

	@Override
	public String getTableName()
	{
		return this.TABLE_NAME;
	}
    
    
//---Metodos Set:
    
	/**
	 * Grava o ID
	 * @param _id ID a ser gravado
	 */
    @Override
    public void setID(int _id)
    {
        this.id = _id;
    }

	/**
	 * @param idAtor the idAtor to set
	 */
	public void setIdAtor(int idAtor)
	{
		this.idAtor = idAtor;
	}

	/**
	 * @param idFilme the idFilme to set
	 */
	public void setIdFilme(int idFilme)
	{
		this.idFilme = idFilme;
	}

	/**
	 * @param nomePersonagem the nomePersonagem to set
	 */
	public void setNomePersonagem(String nomePersonagem)
	{
		this.nomePersonagem = nomePersonagem;
	}

	/**
	 * Ator do papel
	 * @return
	 */
	public String ator()
	{
		return CRUD.crudAtor.buscar(idAtor).getNome();
	}

	public String filme()
	{
		return CRUD.crudFilme.buscar(idFilme).getTitulo();
	}
    
//---Outros metodos:

	public String toString() 
	{
		return "\nID...................:    " + this.id + 
			   "\nNome do personagem...:    " + this.nomePersonagem +
			   "\nAtor.................:    " + ator() + " (id: " + this.idAtor + ")" +
			   "\nFilme................:    " + filme() + " (id: " + this.idFilme + ")";
	}
	
	/**
	 * Transforma o objeto em um array de bytes
	 * @return Array de bytes com os dados
	 */
    @Override
	public byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		dos.writeInt(id);
		dos.writeUTF(nomePersonagem);
		dos.writeInt(idAtor);
		dos.writeInt(idFilme);

		return baos.toByteArray();
	}

	/**
	 * Preenche os campos a partir de um array de bytes
	 * @param _byteData Array de bytes com os dados
	 */
	@Override
	public void fromByteArray(byte[] _byteData) throws IOException 
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(_byteData);
		DataInputStream dis = new DataInputStream(bais);
		
		this.id = dis.readInt();
		this.nomePersonagem = dis.readUTF();
		this.idAtor = dis.readInt();
		this.idFilme = dis.readInt();
	}
	
	public static Object[] papelPorFilme(int idFilme)
	{
		Arquivo<Papel> arq = CRUD.crudPapel.arquivo;

		return arq.buscaPorChave(idFilme, 
										 () ->
										 {
											 try
											 {
										  	  	 arq.skipString();
											  	 arq.skipInt();
											 }

											 catch (IOException e)
											 {
												 throw new UncheckedIOException(e);
											 }
								 		 });
	}

	public static Object[] papelPorAtor(int idAtor)
	{
		Arquivo<Papel> arq = CRUD.crudPapel.arquivo;

		return arq.buscaPorChave(idAtor, 
										() ->
										{
											try
											{
										  		arq.skipString();
											}

											catch (IOException e)
											{
												throw new UncheckedIOException(e);
											}
								 		});
	}
}
