package br.com.desafio.petz.api.model.enuns;

public enum EnumTipo {
	AVE(1), CACHORRO(2), GATO(3), PEIXE(4), OUTROS(5);

	private int id;

	private EnumTipo(int id) {
		this.id = id;
	}

	public int id() {
		return this.id;
	}
}
