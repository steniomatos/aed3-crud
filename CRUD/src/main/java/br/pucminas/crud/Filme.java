package br.pucminas.crud;

import java.io.*;

public class Filme implements Registro
{
    private int id;
    private int categoria;
    private String titulo;
    private short ano;
    private final String TABLE_NAME = "filme";

    public Filme()
    {
        this.id = 0;
        this.titulo = "";
        this.categoria = 0;
        this.ano = 0;
	}
	
    public Filme(String _titulo, int _categoria, short _ano)
    {
        this.titulo = _titulo;
        this.categoria = _categoria;
        this.ano = _ano;
    }

//---Metodos get:

    @Override
	public int getID()
	{
		return this.id;
	}
	  
    public String getTitulo()
	{
		return this.titulo;
	}

	public int getCategoria()
	{
        return this.categoria;
    }
    
	public short getAno()
	{
        return this.ano;
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

    public void setTitulo(String _titulo)
	{
		this.titulo = _titulo;
	}

	public void setCategoria(int _categoria)
	{
        this.categoria = _categoria;
    }
    
    public void setAno(short _ano)
	{
        this.ano = _ano;
	}
    
    public String categoria()
    {
    	return CRUD.crudCategoria.buscar(categoria).getNome();
    }

//---Outros metodos:

	public String toString() 
	{
		return "\nID.......:    " + this.id + 
			   "\nTitulo...:    " + this.titulo + 
			   "\nCategoria:    " + categoria() + " (id: " + this.categoria + ")" + 
			   "\nAno......:    " + this.ano;
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
		dos.writeUTF(titulo);
		dos.writeInt(categoria);
		dos.writeShort(ano);  

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
		this.titulo = dis.readUTF();
		this.categoria = dis.readInt();
		this.ano = dis.readShort();
	}

	public static Object[] filmePorCategoria(int idCategoria)
	{
		Arquivo<Filme> arq = CRUD.crudFilme.arquivo;

		return arq.buscaPorChave(idCategoria, 
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
