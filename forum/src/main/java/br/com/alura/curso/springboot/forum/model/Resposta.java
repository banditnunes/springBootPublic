package br.com.alura.curso.springboot.forum.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NamedEntityGraph(name = "topicoResposta",attributeNodes = {
		@NamedAttributeNode("topico"),
		@NamedAttributeNode("autor")
})

@Entity(name = "resposta")
public class Resposta implements Serializable {

    private static final long serialVersionUID = 6493779264042885741L;
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String mensagem;
	@ManyToOne(fetch = FetchType.LAZY,targetEntity =Topico.class)
	private Topico topico;
	private LocalDateTime dataCriacao = LocalDateTime.now();
	@ManyToOne
	private Usuario autor;
	private Boolean solucao;

	public Resposta(String mensagem, Topico topico, Usuario autor, boolean solucao) {
		this.mensagem=mensagem;
		this.topico=topico;
		this.autor=autor;
		this.solucao=solucao;
	}
	public Resposta(){ }

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
		Resposta other = (Resposta) obj;
		if (id == null) {
			return other.id == null;
		} else return id.equals(other.id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Topico getTopico() {
		return topico;
	}

	public void setTopico(Topico topico) {
		this.topico = topico;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Boolean getSolucao() {
		return solucao;
	}

	public void setSolucao(Boolean solucao) {
		this.solucao = solucao;
	}

}
