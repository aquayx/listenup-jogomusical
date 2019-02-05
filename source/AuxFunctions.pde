// apenas a resposta relativa ao intervalo de tempo atual deve ser aceita
void gameCode()
{
  int millisComp = millisInicial + (int)(tempo*1000 * (posResposta+1));  // determina qual resposta deverá ser aceita
  int millisLimite = millisInicial + (int)(tempo*1000 * (posResposta+2));  // tempo limite para a próxima resposta
  
  /* idéias:
       - verificar se já é hora de aceitar a próxima resposta (millis() > millisComp). millisComp é atualizado a cada nota executada
       - nessa hora, definir que o usuário pode fornecer uma resposta, e ainda não respondeu.
       - parece redundante usar duas variáveis que praticamente dizem a mesma coisa, mas não consegui fazer funcionar com só uma...
   */
  if (millis() > millisComp && !podeResponder)
  {
    podeResponder = true;
    jaRespondeu = false;
  }
  
  // se o usuário não responder a tempo, entra nesse if e mostra em vermelho a flecha que ele deveria pressionar
  if (millis() > millisLimite && playing)
  {
    respostaErrada(notas[posResposta+1] - notas[posResposta]);
  }
}



// desenha as flechas, barra inferior e score
void drawPlayingInterface()
{
  drawArrows();
  initBarButtons();
  drawButtonBar();
  drawButtonArray(barButtons);
  drawScore();
}



// calcula para qual tamanho uma imagem qualquer deve ser redimensionada para encaixar na tela e mantém suas proporções
// o redimensionamento é feito para o tamanho da largura ou da altura (definido pelo parâmetro method)
int[] fitToScreen(PImage img, char method)
{
  int[] dimensoes = new int[2];
  float proporcao;
  
  if (method == 'w')
  {
    proporcao = (float)sketchWidth()/img.width;
    dimensoes[0] = sketchWidth();
    dimensoes[1] = (int)(img.height*proporcao);
  }
  else if (method == 'h')
  {
    proporcao = (float)sketchHeight()/img.height;
    dimensoes[0] = (int)(img.width*proporcao);
    dimensoes[1] = sketchHeight();
  }
  else
  {
    println("parâmetro inválido, use 'w' ou 'h'");
    dimensoes[0] = 0;
    dimensoes[1] = 0;
  }
  
  return dimensoes;
}



// método que retorna o tempo relativo a uma dificuldade
float getTempo(int diff)
{
  float tempo = -1;

  switch(diff)
  {
    case FACIL: tempo = TEMPO_EASY; break;
    case MEDIO: tempo = TEMPO_MEDIUM; break;
    case DIFICIL: tempo = TEMPO_HARD; break;
    default: println("erro ao retornar tempo");
  }
  
  return tempo;
}

