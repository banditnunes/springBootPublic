package br.com.alura.curso.springboot.forum.model;

import javax.persistence.*;
import java.io.Serializable;

@NamedEntityGraph(name = "cursoCategoria", attributeNodes = {
		@NamedAttributeNode(value = "categoria")
})
@Entity(name = "curso")
public class Curso implements Serializable {

	private static final long serialVersionUID = 8835158890941474111L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@ManyToOne
	private Categoria categoria;

	public Curso(String nome, Categoria categoria) {
		this.nome = nome;
		this.categoria = categoria;
	}

	public Curso() {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curso other = (Curso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}
