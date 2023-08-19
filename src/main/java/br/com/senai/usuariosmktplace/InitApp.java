package br.com.senai.usuariosmktplace;

import javax.swing.JOptionPane;

import br.com.senai.usuariosmktplace.core.domain.Usuario;
import br.com.senai.usuariosmktplace.core.service.UsuarioService;

public class InitApp {

	public static void main(String[] args) {
	
		UsuarioService usuar = new UsuarioService();
		
		Usuario zezin = new Usuario(null, "Jose Rubens","jose4321");
		
		usuar.salvar(zezin);
		
		try {
			
			usuar.salvar(zezin);
			System.out.println(zezin.getLogin());
			System.out.println(zezin.getNomeCompleto());
			System.out.println(zezin.getSenha());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
	}
}
