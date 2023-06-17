package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {

	public static Map<String, List<String>> transformarGLCparaFNG(Map<String, List<String>> glc) {
		Map<String, List<String>> fng = new HashMap<>(glc);

		// Passo 1: Remover recursão à esquerda direta
		for (String naoTerminal : fng.keySet()) {
			List<String> producoes = new ArrayList<>(fng.get(naoTerminal));
			List<String> substituicoes = new ArrayList<>();
			List<String> remover = new ArrayList<>();
			for (String producao : producoes) {
				if (producao.startsWith(naoTerminal)) {
					substituicoes.add(producao.substring(1) + naoTerminal + "'");
					remover.add(producao);
				}
			}
			producoes.removeAll(remover);
			if (!substituicoes.isEmpty()) {
				fng.get(naoTerminal).addAll(substituicoes);
			}
		}

		// Passo 2: Introduzir não terminais auxiliares
		String novoNaoTerminal = "Z";
		while (fng.containsKey(novoNaoTerminal)) {
			novoNaoTerminal += "'";
		}
		for (String naoTerminal : fng.keySet()) {
			List<String> producoes = new ArrayList<>(fng.get(naoTerminal));
			List<String> substituicoes = new ArrayList<>();
			List<String> remover = new ArrayList<>();
			for (String producao : producoes) {
				if (producao.startsWith(naoTerminal)) {
					substituicoes.add(producao.substring(1) + novoNaoTerminal);
					remover.add(producao);
				}
			}
			producoes.removeAll(remover);
			if (!substituicoes.isEmpty()) {
				if (!fng.containsKey(novoNaoTerminal)) {
					fng.put(novoNaoTerminal, new ArrayList<>());
				}
				fng.get(novoNaoTerminal).addAll(substituicoes);
				producoes.add(novoNaoTerminal);
			}
		}

		// Passo 3: Remover produções unitárias
		for (String naoTerminal : fng.keySet()) {
			List<String> producoes = new ArrayList<>(fng.get(naoTerminal));
			List<String> substituicoes = new ArrayList<>();
			List<String> remover = new ArrayList<>();
			for (String producao : producoes) {
				if (producao.length() == 1 && fng.containsKey(producao)) {
					substituicoes.addAll(fng.get(producao));
					remover.add(producao);
				}
			}
			producoes.removeAll(remover);
			if (!substituicoes.isEmpty()) {
				fng.get(naoTerminal).addAll(substituicoes);
			}
		}

		// Passo 4: Renomear não terminais
		String novoNome = "S";
		while (fng.containsKey(novoNome)) {
			novoNome += "'";
		}
		String primeiroNaoTerminal = fng.keySet().iterator().next();
		fng.put(novoNome, fng.remove(primeiroNaoTerminal));

		return fng;
	}

	public static void main(String[] args) {
        Map<String, List<String>> glc = new HashMap<>();
        List<String> producoesS = new ArrayList<>();
        producoesS.add("AB");
        producoesS.add("SCB");
        
        List<String> producoesA = new ArrayList<>();
        producoesA.add("aA");
        producoesA.add("C");
        glc.put("A", producoesA);
        List<String> producoesB = new ArrayList<>();
        producoesB.add("bB");
        producoesB.add("b");
        glc.put("B", producoesB);
        List<String> producoesC = new ArrayList<>();
        producoesC.add("cC");
        producoesC.add("y");
        glc.put("C", producoesC);

        Map<String, List<String>> fng = transformarGLCparaFNG(glc);

        System.out.println("Gramática resultante na Forma Normal de Greibach (FNG):");
        for (String naoTerminal : fng.keySet()) {
            List<String> producoes = fng.get(naoTerminal);
            for (String producao : producoes) {
                System.out.println(naoTerminal + " -> " + producao);
            }
        }
    }
}