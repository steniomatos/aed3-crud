package br.pucminas.crud;

public class CrudPapel extends CRUDAbstract<Papel>
{	
	public CrudPapel()
	{
		super(Papel.class);
	}

	@Override
	public void printarMenu()
	{
		Menus.menu("PAPEIS",
			new String[]
			{ "Listar papeis", "Buscar papel", "Incluir papel", "Excluir papel", "Modificar papel" },
			new Runnable[]
			{
				() -> listarEPrintar(),
				() -> buscarPapel(),
				() -> incluirPapel(),
				() -> excluirPapel(),
				() -> alterarPapel(),
			}
		);
	}

	//------------------METODOS COM PAPEL

	/**
	 * Busca um filme presente no arquivo
	 */
	public void buscarPapel()
	{
		IO.println("\nBUSCA");

		int id = IO.readint("ID: ");
		
		if (id <= 0) return;

		buscarEPrintar(id);
	}

	/**
	 * Cadastra um novo filme
	 */
	public void incluirPapel()
	{
		int idAtor, idFilme;
		String nomePersonagem;
		char confirma;

		IO.println("\nINCLUSÃO");

		nomePersonagem = IO.readLine("Nome do personagem: ");
		
		IO.println ("Atores Disponiveis:");
		
		CRUD.crudAtor.listarEPrintar();
		
		idAtor = IO.readint("Id Ator: ");
		
		//Verificar se existe a categoria
		if (CRUD.crudAtor.buscar(idAtor) == null)
		{
			IO.println("O id do ator não existe");
			
			return;
		}

		IO.println ("Filmes Disponiveis:");
		
		CRUD.crudFilme.listarEPrintar();
		
		idFilme = IO.readint("Id Filme: ");
		
		//Verificar se existe a categoria
		if (CRUD.crudFilme.buscar(idFilme) == null)
		{
			IO.println("O id do filme não existe");
			
			return;
		}

		String resposta = IO.readLine("\nConfirma inclusão (s-n)? ");
		
		if (resposta.isEmpty()) resposta = "n";
		
		confirma = resposta.charAt(0);

		if(confirma == 's' || confirma == 'S')
		{
			Papel aSerIncluido = new Papel(idAtor, idFilme, nomePersonagem);
			int id = incluir(aSerIncluido);
			IO.println("Papel incluído com ID: " + id);
		}
		else
			IO.println("Papel não incluído.");

		IO.pause();
	}

	/**
	 * Exclui um filme do arquivo
	 */
	public void excluirPapel()
	{
		int id;
		char confirma;

		IO.println("\nEXCLUSÃO");

		id = IO.readint("ID: ");

		if (id <= 0) return;

		Papel aSerExcluido = buscar(id);

		if (aSerExcluido != null)
		{
			IO.println(aSerExcluido);

			String resposta = IO.readLine("\nConfirma exclusão (s-n)? ");
			
			if (resposta.isEmpty()) resposta = "n";
			
			confirma = resposta.charAt(0);
			
			if(confirma=='s' || confirma=='S')
				if(excluir(id))
					IO.println("Papel excluído.");
				else
					IO.println("Exclusão cancelada.");
		}
		else
			IO.println("Papel não encontrado");

		IO.pause();
	}

	/**
	 * Altera um filme no arquivo
	 */
	public void alterarPapel()
	{
		IO.println("\nALTERAÇÃO");

		int id;
		char confirma;

		id = IO.readint("ID: ");
		
		if (id <= 0) return;

		Papel aSerAlterado = buscar(id);

		if(aSerAlterado != null)
			IO.println(aSerAlterado);
		else
		{
			IO.println("Papel não encontrado");
			return;
		}

		IO.println();

		int idAtor, idFilme;
		String nomePersonagem;

		nomePersonagem = IO.readLine("Novo nome do personagem: ");
		if (nomePersonagem.trim().equals(""))
			nomePersonagem = aSerAlterado.getNomePersonagem();

		idAtor = IO.readint("Novo ator: ");
		if (idAtor == -1)
			idAtor = aSerAlterado.getIdAtor();

		idFilme = IO.readint("Novo filme: ");
		if (idFilme == -1) 
			idFilme = aSerAlterado.getIdFilme();

		String resposta = IO.readLine("\nConfirma alteração (s-n)? ");
		
		if (resposta.isEmpty()) resposta = "n";
		
		confirma = resposta.charAt(0);

		if(confirma == 's' || confirma == 'S')
		{
			aSerAlterado = new Papel(idAtor, idFilme, nomePersonagem);
			aSerAlterado.setID(id);

			if (alterar(aSerAlterado))
				IO.println("Papel alterado com sucesso.");
			else
				IO.println("Erro ao alterar filme.");
		}
		else
			IO.println("Alteração cancelada.");

		IO.pause();
	}
}
