package br.pucminas.crud;

import java.io.*;

/**
 * @author Mateus Auler <mateus.auler@sga.pucminas.br>
 */

public class Ator implements Registro
{
    private int id;
    private String nome;
    private byte idade;
    private final String TABLE_NAME = "ator";

    public Ator()
    {
        this.id = 0;
        this.nome = "";
        this.idade = 0;
	}
	
    public Ator(String _nome, byte _idade)
    {
        this.nome = _nome;
        this.idade = _idade;
    }

//---Metodos get:

    @Override
	public int getID()
	{
		return this.id;
	}
	  
    public String getNome()
	{
		return this.nome;
	}
    
	public byte getIdade()
	{
        return this.idade;
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

    public void setnome(String _nome)
	{
		this.nome = _nome;
	}

    public void setidade(byte _idade)
	{
        this.idade = _idade;
	}
    
//---Outros metodos:

	public String toString() 
	{
		return "\nID.......:    " + this.id + 
			   "\nNome.....:    " + this.nome +
			   "\nIdade....:    " + this.idade;
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
		dos.writeUTF(nome);
		dos.writeByte(idade);

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
		this.nome = dis.readUTF();
		this.idade = dis.readByte();
	}
	
}
