package br.com.alura.curso.springboot.forum.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NamedEntityGraph(name = "topicoComRespostas",
		attributeNodes = {
				@NamedAttributeNode("titulo"),
				@NamedAttributeNode(value = "curso",subgraph = "categoria-subgraph"),
				@NamedAttributeNode(value = "respostas"),
				@NamedAttributeNode(value = "autor",subgraph = "perfis-subgraph")
			},
		subgraphs = {
				@NamedSubgraph(name = "categoria-subgraph",attributeNodes = {
						@NamedAttributeNode("categoria")
				}),
				@NamedSubgraph(name = "perfis-subgraph", attributeNodes = {
						@NamedAttributeNode("perfis")
				})
		}

)
@NamedEntityGraph(name = "topicoPorTitulo",attributeNodes = {
		@NamedAttributeNode(value = "curso")
})

@Entity(name = "topico")
public class Topico implements Serializable {

	private static final long serialVersionUID = 8679444543759479482L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao = LocalDateTime.now();
	@Enumerated(EnumType.STRING)
	private StatusTopico status = StatusTopico.NAO_RESPONDIDO;
	@ManyToOne
	private Usuario autor;
	@ManyToOne(cascade = CascadeType.REMOVE)
	private Curso curso;
	@OneToMany(mappedBy = "topico",fetch = FetchType.LAZY)
	private List<Resposta> respostas ;

	public Topico() {
	}

	public Topico(String titulo, String mensagem, Curso curso,Usuario autor) {
		this.titulo = titulo;
		this.mensagem = mensagem;
		this.curso = curso;
		this.autor=autor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Topico topico = (Topico) o;
		return Objects.equals(id, topico.id) &&
				Objects.equals(titulo, topico.titulo) &&
				Objects.equals(mensagem, topico.mensagem) &&
				Objects.equals(dataCriacao, topico.dataCriacao) &&
				status == topico.status &&
				Objects.equals(autor, topico.autor) &&
				Objects.equals(curso, topico.curso) &&
				Objects.equals(respostas, topico.respostas);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, titulo, mensagem, dataCriacao, status, autor, curso, respostas);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public StatusTopico getStatus() {
		return status;
	}

	public void setStatus(StatusTopico status) {
		this.status = status;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Resposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<Resposta> respostas) {
		this.respostas = respostas;
	}
}
