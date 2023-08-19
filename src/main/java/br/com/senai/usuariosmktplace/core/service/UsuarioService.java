package br.com.senai.usuariosmktplace.core.service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.senai.usuariosmktplace.core.dao.DaoUsuario;
import br.com.senai.usuariosmktplace.core.dao.FactoryDao;
import br.com.senai.usuariosmktplace.core.domain.Usuario;

public class UsuarioService {

	private DaoUsuario dao;
	
	public UsuarioService() {
		
		this.dao = FactoryDao.getInstance().getDaoUsuario();
	}
	
	
	public void salvar (Usuario usuario) {
		this.validar(usuario);
	    String login = this.gerarLoginPor(usuario.getNomeCompleto());
	    usuario.setLogin(login);
	    
	    this.dao.inserirUsuario(usuario);
	}
	
	public Usuario criarLogin(String login) {
		
		 Usuario usuarioLocalizado = this.dao.buscarPor(login);
		if(usuarioLocalizado != null) {
			throw new IllegalArgumentException("Login já cadastrado no banco de dados");
		}else {
			
			
		}
		 
		return null;
	}
	
	public void validar(Usuario usuario) {
		if (usuario != null) {
			
				
			boolean isNomeInvalido = usuario.getNomeCompleto() == null
					|| usuario.getNomeCompleto().isBlank()
					|| usuario.getNomeCompleto().length() > 120
					|| usuario.getNomeCompleto().length() < 5
					|| !usuario.getNomeCompleto().contains(" ");
		
			if(isNomeInvalido) {		
				throw new IllegalArgumentException("O nome do usuário é obrigatório e deve possuir entre 5 a 120 caracteres");
			}
			
			boolean isSenhaInvalida = usuario.getSenha() == null
					|| usuario.getSenha().isBlank()
					|| usuario.getSenha().length() > 15
					|| usuario.getSenha().length() < 6
					|| !usuario.getSenha().matches("^(?=.*[a-zA-Z])(?=.*\\d).*$");
			if(isSenhaInvalida) {			
				throw new IllegalArgumentException("A senha é obrigatória! Deve conter entre 6 a 15 caracteres "
					+ "e possuir números e letras");
			}	
		
		}else {
			throw new NullPointerException("O usuário não pode possuir campos nulos");
		}
	}
	
	public List<String> fracionar(String nomeCompleto) {
        List<String> nomeFracionado = new ArrayList<String>();
        

            String[] partesDoNome = nomeCompleto.split(" ");

                for (String parte : partesDoNome) {
                    boolean isNaoContemArtigo = !parte.equalsIgnoreCase("de") 
                            && !parte.equalsIgnoreCase("da")
                            && !parte.equalsIgnoreCase("do")
                            && !parte.equalsIgnoreCase("e")
                            && !parte.equalsIgnoreCase("dos")
                            && !parte.equalsIgnoreCase("das");

                    if (isNaoContemArtigo) {
                        nomeFracionado.add(parte.toLowerCase());
                    }
                }
                return nomeFracionado;
    }
	
	
	public String gerarLoginPor(String nomeCompleto) {
        nomeCompleto = removerAcentoDo(nomeCompleto);
        List<String> partesDoNome = fracionar(nomeCompleto);
        String loginGerado = null;
        Usuario usuarioEncontrado = null;
        if (!partesDoNome.isEmpty()) {
            for (int i = 1; i < partesDoNome.size(); i++) {
                    loginGerado = partesDoNome.get(0) + "." + partesDoNome.get(i);
                    usuarioEncontrado = dao.buscarPor(loginGerado);
                    if (usuarioEncontrado == null) {
                        return loginGerado;
                    }
            }

            int proximoSequencial = 0;
            String loginDisponivel = null;
                while(usuarioEncontrado != null) {
                    loginDisponivel = loginGerado + ++proximoSequencial;
                    usuarioEncontrado = dao.buscarPor(loginDisponivel);
                }
                loginGerado = loginDisponivel;

                if (loginGerado.length() > 5 && loginGerado.length() < 50) {
                    return loginGerado;
                } else {
                    throw new IllegalArgumentException("Forneça um nome e sobrenome que fique entre 5 e 50 caracteres!");
                }
        }

        throw new IllegalArgumentException("erro o nome não pode ser nulo");

    }
	
	public String removerAcentoDo(String nomeCompleto) {
		
	return Normalizer.normalize(nomeCompleto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]","");
		
	}
	
	
}







