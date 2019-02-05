/*
   ListenUp! Um jogo musical.

   Trabalho desenvolvido como parte da avaliação da disciplina de Algoritmos I do curso
   de Bacharelado em Sistemas de Informação da Universidade Tecnológica Federal do Paraná,
   campus Curitiba, no primeiro semestre de 2014.

   Estudantes: Caroline Alves da Silva
               Jorge Luiz dos Santos Ramos Junior

   Orientadora: Sílvia Amélia Bim

   Tipo de licença: CC BY-NC-ND

   Data: 25/08/2014
*/


import ddf.minim.*;
import ddf.minim.ugens.*;


// a maioria das declarações de variáveis são feitas no arquivo Vars
// a maioria das declarações de funções são feitas no arquivo AuxFunctions


// para começar, mostra a tela de loading. as coisas são carregadas no draw (if(frameCount<=1))
void setup()
{
  loadingScreen();
}



void draw()
{
  // se frameCount <= 1, significa que o programa ainda não foi carregado
  if(frameCount <= 1)
  {
    load();
  }
  
  // else, os recursos do programa já foram carregados. desenhar alguma das telas
  else
  {
    switch(tela)
    {
      case DIFF_SELECT: difficultySelect(); break;
      case JOGO_FACIL: easyMode(); break;
      case JOGO_MEDIO: mediumMode(); break;
      case JOGO_DIFICIL: hardMode(); break;
      case END_SCREEN: endScreen(); break;
      default: difficultySelect(); println("erro ao mudar de tela");
    }
  }  
}



void mousePressed()
{
  // verifica qual set de botões deve ser usado de acordo com a tela atual 
  if (tela == DIFF_SELECT && loaded) // só permite que os botões sejam clicados após o jogo ser carregado, para evitar acessos à morte
  {
    for (int i = 0; i < diffButtons.length; i++)
    {
      diffButtons[i].action();
    }
  }
  
  else if (tela == JOGO_FACIL || tela == JOGO_MEDIO || tela == JOGO_DIFICIL)
  {
    for (int i = 0; i < barButtons.length; i++)
    {
      barButtons[i].action();
    }
  }
  
  // se o mouse for pressionado durante a tela final, voltar ao começo
  else if (tela == END_SCREEN)
  {
    tela = DIFF_SELECT;
  }
}



void keyPressed()
{
  if (playing) // o programa só deve aceitar respostas se há um jogo em andamento
  {
    if (tela == JOGO_FACIL || tela == JOGO_MEDIO || tela == JOGO_DIFICIL) // redundância para mais segurança (redundante pois se o usuário está jogando, ele deve estar em uma dessas telas)
    {
      if (keyCode == UP)
      {
        enviaResposta(SUBIU);
      }
      else if (keyCode == DOWN)
      {
        enviaResposta(DESCEU);
      }
      else if (keyCode == RIGHT)
      {
        enviaResposta(IGUAL);
      }
    }
  }
  
  else // se as condições forem corretas, também aceita as teclas enter ou espaço como hotkeys para iniciar um jogo
  {
    if (tela == JOGO_FACIL || tela == JOGO_MEDIO || tela == JOGO_DIFICIL)
    {
      if (keyCode == ENTER || keyCode == ' ')
      {
        if (!playing) notas = playSound(N_NOTAS, tempo);
      }
    }
  }
  
  if (tela == END_SCREEN) // se qualquer tecla for apertada durante a tela final, voltar ao começo
  {
    tela = DIFF_SELECT;
  }
}



// para rodar o programa em fullscreen
boolean sketchFullScreen()
{
  return true;
}
