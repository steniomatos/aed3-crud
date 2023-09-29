package br.pucminas.crud;

public class CRUD
{
	public static CrudFilme crudFilme = new CrudFilme();
	public static CrudCategoria crudCategoria = new CrudCategoria();
	public static CrudAtor crudAtor = new CrudAtor();
	public static CrudPapel crudPapel = new CrudPapel();
	
	public static void main(String[] args)
	{    
		try
		{
			escolherMenus();
			crudFilme.fechar();
			crudCategoria.fechar();
			crudAtor.fechar();
			crudPapel.fechar();
		}
		
		catch(Exception e)
		{
	   		e.printStackTrace();
		}        
	}    
	
	//------------------METODOS PARA PRINTAR O MENU
	
	/**
	 * Metodo para escolher sub menus principal
	 */
	public static void escolherMenus()
	{
		Menus.menu("PRINCIPAL",
			new String[]
			{ "Filmes", "Categorias", "Atores", "Papéis", "Povoar Banco de Dados" },
			new Runnable[]
			{
				() -> crudFilme.printarMenu(),
				() -> crudCategoria.printarMenu(),
				() -> crudAtor.printarMenu(),
				() -> crudPapel.printarMenu(),
				() -> povoar(),
			}
		);
	}
	
	/**
	 * Gerar uma lista de filmes
	 */
	public static void povoar()
	{
		try
		{
			int idCrime = crudCategoria.incluir(new Categoria("Crime"));
			int idAnima = crudCategoria.incluir(new Categoria("Animação"));
			int idScifi = crudCategoria.incluir(new Categoria("Ficção científica"));

			int idGodf = crudFilme.incluir(new Filme("The Godfather", idCrime, (short)1972));
			int idKimi = crudFilme.incluir(new Filme("Kimi no na wa.", idAnima, (short)2016));
			int idKoen = crudFilme.incluir(new Filme("Koe no Katachi", idAnima, (short)2017));
			int idStar = crudFilme.incluir(new Filme("Star Wars", idScifi, (short)1977));

			int idMarl = crudAtor.incluir(new Ator("Marlon Brando", (byte)80));
			int idMone = crudAtor.incluir(new Ator("Mone Kamishiraishi", (byte)21));
			int idSaor = crudAtor.incluir(new Ator("Saori Hayami", (byte)28));
			int idMark = crudAtor.incluir(new Ator("Mark Hamill", (byte)68));

			crudPapel.incluir(new Papel(idMarl, idGodf, "Vito Corleone"));
			crudPapel.incluir(new Papel(idMone, idKimi, "Mitsuha Miyamizu"));
			crudPapel.incluir(new Papel(idSaor, idKoen, "Nishimiya Shouko"));
			crudPapel.incluir(new Papel(idMark, idStar, "Luke Skywalker"));
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
}
