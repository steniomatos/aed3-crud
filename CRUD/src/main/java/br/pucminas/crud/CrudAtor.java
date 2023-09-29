package br.pucminas.crud;

/**
 * CrudAltores
 * @author Mateus Auler <mateus.auler@sga.pucminas.br>
 */
public class CrudAtor extends CRUDAbstract<Ator>
{
    public CrudAtor()
	{
		super(Ator.class);
	}

	@Override
	public void printarMenu()
	{
		Menus.menu("ATORES",
			new String[]
			{ "Listar atores", "Buscar ator", "Incluir ator", "Excluir ator", "Modificar ator", "Papéis por ator" },
			new Runnable[]
			{
				() -> listarEPrintar(),
				() -> buscarAtor(),
				() -> incluirAtor(),
				() -> excluirAtor(),
				() -> alterarAtor(),
				() -> exibirPapeis(),
			}
		);
	}

	//------------------METODOS COM ATOR

	/**
	 * Busca um ator presente no arquivo
	 */
	public void buscarAtor()
	{
		IO.println("\nBUSCA");

		int id = IO.readint("ID: ");
		
		if (id <= 0) return;

		buscarEPrintar(id);
	}

	/**
	 * Cadastra um novo Ator
	 */
	public void incluirAtor()
	{
		String nome;
		byte idade;
		char confirma;

		IO.println("\nINCLUSÃO");

		nome = IO.readLine("Nome: ");

		idade = IO.readbyte("Idade: ");

		String resposta = IO.readLine("\nConfirma inclusão (s-n)? ");
		
		if (resposta.isEmpty()) resposta = "n";
		
		confirma = resposta.charAt(0);

		if(confirma == 's' || confirma == 'S')
		{
			Ator aSerIncluido = new Ator(nome, idade);
			int id = incluir(aSerIncluido);
			IO.println("Ator incluído com ID: " + id);
		}
		else
			IO.println("Ator não incluído.");

		IO.pause();
	}

	/**
	 * Exclui um Ator do arquivo
	 */
	public void excluirAtor()
	{
		int id;
		char confirma;

		IO.println("\nEXCLUSÃO");

		id = IO.readint("ID: ");

		if (id <= 0) return;

		Ator aSerExcluido = buscar(id);

		if (aSerExcluido != null)
		{
			IO.println(aSerExcluido);

			String resposta = IO.readLine("\nConfirma exclusão (s-n)? ");
			
			if (resposta.isEmpty()) resposta = "n";
			
			confirma = resposta.charAt(0);
			
			if(confirma=='s' || confirma=='S')
				if(excluir(id))
					IO.println("Ator excluído.");
				else
					IO.println("Exclusão cancelada.");
		}
		else
			IO.println("Ator não encontrado");

		IO.pause();
	}

	/**
	 * Altera um Ator no arquivo
	 */
	public void alterarAtor()
	{
		IO.println("\nALTERAÇÃO");

		int id;
		char confirma;

		id = IO.readint("ID: ");
		
		if (id <= 0) return;

		Ator aSerAlterado = buscar(id);

		if(aSerAlterado != null)
			IO.println(aSerAlterado);
		else
		{
			IO.println("Ator não encontrado");
			return;
		}

		IO.println();

		String nome;
		byte idade;

		nome = IO.readLine("Novo nome: ");
		if (nome.equals(" "))
			nome = aSerAlterado.getNome();

		idade = IO.readbyte("Nova idade: ");

		if (idade == -1) idade = aSerAlterado.getIdade();

		String resposta = IO.readLine("\nConfirma alteração (s-n)? ");
		
		if (resposta.isEmpty()) resposta = "n";
		
		confirma = resposta.charAt(0);

		if(confirma == 's' || confirma == 'S')
		{
			aSerAlterado = new Ator(nome, idade);
			aSerAlterado.setID(id);

			if (alterar(aSerAlterado))
				IO.println("Ator alterado com sucesso.");
			else
				IO.println("Erro ao alterar Ator.");
		}
		else
			IO.println("Alteração cancelada.");

		IO.pause();
	}

	public void exibirPapeis()
	{
		int idAtor = IO.readint("ID do ator: ");

		Object[] papeis = Papel.papelPorFilme(idAtor);

		for (int i = 0; i < papeis.length; i++)
			IO.println(papeis[i]);

		IO.pause();
	}
}
