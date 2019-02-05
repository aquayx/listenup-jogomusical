import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ListenUp extends PApplet {

/*
   ListenUp! Um jogo musical.

   Trabalho desenvolvido como parte da avalia\u00e7\u00e3o da disciplina de Algoritmos I do curso
   de Bacharelado em Sistemas de Informa\u00e7\u00e3o da Universidade Tecnol\u00f3gica Federal do Paran\u00e1,
   campus Curitiba, no primeiro semestre de 2014.

   Estudantes: Caroline Alves da Silva
               Jorge Luiz dos Santos Ramos Junior

   Orientadora: S\u00edlvia Am\u00e9lia Bim

   Tipo de licen\u00e7a: CC BY-NC-ND

   Data: 25/08/2014
*/






// a maioria das declara\u00e7\u00f5es de vari\u00e1veis s\u00e3o feitas no arquivo Vars
// a maioria das declara\u00e7\u00f5es de fun\u00e7\u00f5es s\u00e3o feitas no arquivo AuxFunctions


// para come\u00e7ar, mostra a tela de loading. as coisas s\u00e3o carregadas no draw (if(frameCount<=1))
public void setup()
{
  loadingScreen();
}



public void draw()
{
  // se frameCount <= 1, significa que o programa ainda n\u00e3o foi carregado
  if(frameCount <= 1)
  {
    load();
  }
  
  // else, os recursos do programa j\u00e1 foram carregados. desenhar alguma das telas
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



public void mousePressed()
{
  // verifica qual set de bot\u00f5es deve ser usado de acordo com a tela atual 
  if (tela == DIFF_SELECT && loaded) // s\u00f3 permite que os bot\u00f5es sejam clicados ap\u00f3s o jogo ser carregado, para evitar acessos \u00e0 morte
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
  
  // se o mouse for pressionado durante a tela final, voltar ao come\u00e7o
  else if (tela == END_SCREEN)
  {
    tela = DIFF_SELECT;
  }
}



public void keyPressed()
{
  if (playing) // o programa s\u00f3 deve aceitar respostas se h\u00e1 um jogo em andamento
  {
    if (tela == JOGO_FACIL || tela == JOGO_MEDIO || tela == JOGO_DIFICIL) // redund\u00e2ncia para mais seguran\u00e7a (redundante pois se o usu\u00e1rio est\u00e1 jogando, ele deve estar em uma dessas telas)
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
  
  else // se as condi\u00e7\u00f5es forem corretas, tamb\u00e9m aceita as teclas enter ou espa\u00e7o como hotkeys para iniciar um jogo
  {
    if (tela == JOGO_FACIL || tela == JOGO_MEDIO || tela == JOGO_DIFICIL)
    {
      if (keyCode == ENTER || keyCode == ' ')
      {
        if (!playing) notas = playSound(N_NOTAS, tempo);
      }
    }
  }
  
  if (tela == END_SCREEN) // se qualquer tecla for apertada durante a tela final, voltar ao come\u00e7o
  {
    tela = DIFF_SELECT;
  }
}



// para rodar o programa em fullscreen
public boolean sketchFullScreen()
{
  return true;
}
// apenas a resposta relativa ao intervalo de tempo atual deve ser aceita
public void gameCode()
{
  int millisComp = millisInicial + (int)(tempo*1000 * (posResposta+1));  // determina qual resposta dever\u00e1 ser aceita
  int millisLimite = millisInicial + (int)(tempo*1000 * (posResposta+2));  // tempo limite para a pr\u00f3xima resposta
  
  /* id\u00e9ias:
       - verificar se j\u00e1 \u00e9 hora de aceitar a pr\u00f3xima resposta (millis() > millisComp). millisComp \u00e9 atualizado a cada nota executada
       - nessa hora, definir que o usu\u00e1rio pode fornecer uma resposta, e ainda n\u00e3o respondeu.
       - parece redundante usar duas vari\u00e1veis que praticamente dizem a mesma coisa, mas n\u00e3o consegui fazer funcionar com s\u00f3 uma...
   */
  if (millis() > millisComp && !podeResponder)
  {
    podeResponder = true;
    jaRespondeu = false;
  }
  
  // se o usu\u00e1rio n\u00e3o responder a tempo, entra nesse if e mostra em vermelho a flecha que ele deveria pressionar
  if (millis() > millisLimite && playing)
  {
    respostaErrada(notas[posResposta+1] - notas[posResposta]);
  }
}



// desenha as flechas, barra inferior e score
public void drawPlayingInterface()
{
  drawArrows();
  initBarButtons();
  drawButtonBar();
  drawButtonArray(barButtons);
  drawScore();
}



// calcula para qual tamanho uma imagem qualquer deve ser redimensionada para encaixar na tela e mant\u00e9m suas propor\u00e7\u00f5es
// o redimensionamento \u00e9 feito para o tamanho da largura ou da altura (definido pelo par\u00e2metro method)
public int[] fitToScreen(PImage img, char method)
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
    println("par\u00e2metro inv\u00e1lido, use 'w' ou 'h'");
    dimensoes[0] = 0;
    dimensoes[1] = 0;
  }
  
  return dimensoes;
}



// m\u00e9todo que retorna o tempo relativo a uma dificuldade
public float getTempo(int diff)
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

public void drawButtonBar()
{
  // calcula a posi\u00e7\u00e3o Y do topo do ret\u00e2ngulo transparente de acordo com o tamanho da janela
  yTopo = sketchHeight() - sketchHeight()/6;
  
  // desenha a linha branca
  stroke(255);
  line(0, yTopo, sketchWidth(), yTopo);
  
  // desenha o ret\u00e2ngulo transparente
  noStroke();
  fill(255, 255, 255, 128);
  rect(0, yTopo, sketchWidth(), sketchHeight()-yTopo);
  
  // desenha os bot\u00f5es
  image(playBtnImg, 15*sketchWidth()/32-playBtnImg.width/2, (yTopo+sketchHeight())/2-playBtnImg.height/2);
  image(repeatBtnImg, 17*sketchWidth()/32-repeatBtnImg.width/2, (yTopo+sketchHeight())/2-repeatBtnImg.height/2);
  image(backBtnImg, 13*sketchWidth()/32-repeatBtnImg.width/2, (yTopo+sketchHeight())/2-repeatBtnImg.height/2);
  image(fwdBtnImg, 19*sketchWidth()/32-repeatBtnImg.width/2, (yTopo+sketchHeight())/2-repeatBtnImg.height/2);
}



// desenha as tr\u00eas flechas de acordo com o estado atual do jogo (estado inicial/houve um acerto/houve um erro)
public void drawArrows()
{
  if (arrowState == INI) // estado inicial
  {
    image(downArrowImg, sketchWidth()/4-downArrowImg.width/2, sketchHeight()/6);
    image(upArrowImg, sketchWidth()/2-upArrowImg.width/2, sketchHeight()/6);
    image(rightArrowImg, sketchWidth()/2+sketchWidth()/4-rightArrowImg.width/2, sketchHeight()/6+rightArrowImg.height/2);
  }
  else if (arrowState == DOWN_R) // estado quando se acerta um "desceu" (seta para baixo verde, outras em seu estado normal)
  {
    image(greenDownArrowImg, sketchWidth()/4-downArrowImg.width/2, sketchHeight()/6);
    image(upArrowImg, sketchWidth()/2-upArrowImg.width/2, sketchHeight()/6);
    image(rightArrowImg, sketchWidth()/2+sketchWidth()/4-rightArrowImg.width/2, sketchHeight()/6+rightArrowImg.height/2);
  }
  else if (arrowState == UP_R) // estado quando se acerta um "subiu" (seta para cima verde, outras em seu estado normal)
  {
    image(downArrowImg, sketchWidth()/4-downArrowImg.width/2, sketchHeight()/6);
    image(greenUpArrowImg, sketchWidth()/2-upArrowImg.width/2, sketchHeight()/6);
    image(rightArrowImg, sketchWidth()/2+sketchWidth()/4-rightArrowImg.width/2, sketchHeight()/6+rightArrowImg.height/2);
  }
  else if (arrowState == RIGHT_R) // etc...
  {
    image(downArrowImg, sketchWidth()/4-downArrowImg.width/2, sketchHeight()/6);
    image(upArrowImg, sketchWidth()/2-upArrowImg.width/2, sketchHeight()/6);
    image(greenRightArrowImg, sketchWidth()/2+sketchWidth()/4-rightArrowImg.width/2, sketchHeight()/6+rightArrowImg.height/2);
  }
  else if (arrowState == DOWN_W)
  {
    image(redDownArrowImg, sketchWidth()/4-downArrowImg.width/2, sketchHeight()/6);
    image(upArrowImg, sketchWidth()/2-upArrowImg.width/2, sketchHeight()/6);
    image(rightArrowImg, sketchWidth()/2+sketchWidth()/4-rightArrowImg.width/2, sketchHeight()/6+rightArrowImg.height/2);
  }
  else if (arrowState == UP_W)
  {
    image(downArrowImg, sketchWidth()/4-downArrowImg.width/2, sketchHeight()/6);
    image(redUpArrowImg, sketchWidth()/2-upArrowImg.width/2, sketchHeight()/6);
    image(rightArrowImg, sketchWidth()/2+sketchWidth()/4-rightArrowImg.width/2, sketchHeight()/6+rightArrowImg.height/2);
  }
  else if (arrowState == RIGHT_W)
  {
    image(downArrowImg, sketchWidth()/4-downArrowImg.width/2, sketchHeight()/6);
    image(upArrowImg, sketchWidth()/2-upArrowImg.width/2, sketchHeight()/6);
    image(redRightArrowImg, sketchWidth()/2+sketchWidth()/4-rightArrowImg.width/2, sketchHeight()/6+rightArrowImg.height/2);
  }
  else
  {
    println("algo deu errado ao determinar o estado das flechas: " + arrowState);
  }
}



public void drawScore()
{
  // calcula o tamanho e posi\u00e7\u00e3o dos componentes de score
  int rectWidth = (int)(sketchWidth()*0.13f);
  int rectHeight = (int)(sketchHeight()*0.12f);
  int rectXPos = sketchWidth() - rectWidth;
  int rectYPos = sketchHeight()/22;
  
  
  // desenha o ret\u00e2ngulo verde
  fill(goodColor);  
  rect(rectXPos, rectYPos, rectWidth, rectHeight);
  
  // desenha o c\u00edrculo branco (podemos usar os tamanhos que descobrimos para o rect, j\u00e1 que eles possuem rela\u00e7\u00e3o entre si)
  fill(whiteColor);
  ellipse(sketchWidth()-rectWidth, sketchHeight()/22+rectHeight/2, rectHeight, rectHeight);
 
  // desenha o texto de score 
  textAlign(CENTER, CENTER);
  textSize(rectHeight/3);
  text(score, rectXPos + 3*rectWidth/5, rectYPos+rectHeight/2);
  
  // desenha texto de +10 ou -5 com a cor apropriada
  if (arrowState > INI) // m\u00e9todo interessante de verificar se a \u00faltima resposta enviada pelo usu\u00e1rio foi correta
  {
    fill(goodColor);
    text("+10", sketchWidth()-rectWidth, sketchHeight()/22+rectHeight/2);
  }
  else if (arrowState < INI)
  {
    fill(badColor);
    text("-5", sketchWidth()-rectWidth, sketchHeight()/22+rectHeight/2);
  }
}
public void loadingScreen()
{
  background(255,204,42);
  loadingImg = loadImage("img_abertura.png");
  
  // fator de redimensionamento da imagem
  final float FATOR = 0.8f;
  
  // calcula a propor\u00e7\u00e3o em que a imagem de carregamento deve ser redimensionada
  int[] dimensoes = fitToScreen(loadingImg, 'h');

  // centraliza a imagem e redimensiona de acordo com o fator
  loadingImg.resize((int)(dimensoes[0]*FATOR), (int)(dimensoes[1]*FATOR));
  image(loadingImg, width/2-loadingImg.width/2, height/2-loadingImg.height/2); // centraliza
}



// inicializa e carrega os objetos
public void load()
{
  // inicializa os objetos de \u00e1udio
  minim = new Minim(this);
  out = minim.getLineOut();
  
  // prepara as imagens
  loadDifficultyImgs();
  loadArrows();
  loadBarImgs();
  loadEasyImgs();
  loadMediumImgs();
  loadHardImgs();
  loadEndImgs();
}



/* abaixo, apenas fun\u00e7\u00f5es que preparam imagens (carregam e redimensionam de acordo com as dimens\u00f5es da tela) */

public void loadDifficultyImgs()
{
  diffBgImg = loadImage("bg_menu.jpg");
  galeraImg = loadImage("img_galera.png");
  etiquetaImg = loadImage("img_etiqueta.png");
  facilImg = loadImage("dif_facil.png");
  medioImg = loadImage("dif_medio.png");
  dificilImg = loadImage("dif_dificil.png");
  
  // calcula um fator de redimensionamento para a etiqueta e as imagens de dificuldade
  // isso \u00e9 necess\u00e1rio para que a interface seja apresent\u00e1vel em qualquer tamanho de tela
  final float FATOR_ETIQUETA = sketchWidth()*0.12f/etiquetaImg.width;
  final float FATOR_DIFF = sketchHeight()*0.12f/facilImg.height;

  // redimensiona o background para o tamanho da tela
  diffBgImg.resize(sketchWidth(), sketchHeight());

  // redimensiona a galera proporcionalmente \u00e0 largura da tela
  int[] dimensoesGalera = fitToScreen(galeraImg, 'w');
  galeraImg.resize(dimensoesGalera[0], dimensoesGalera[1]);
  
  // redimensiona as imagens de acordo com o fator calculado
  etiquetaImg.resize((int)(etiquetaImg.width*FATOR_ETIQUETA), (int)(etiquetaImg.height*FATOR_ETIQUETA));
  facilImg.resize((int)(facilImg.width*FATOR_DIFF), (int)(facilImg.height*FATOR_DIFF));
  medioImg.resize((int)(medioImg.width*FATOR_DIFF), (int)(medioImg.height*FATOR_DIFF));
  dificilImg.resize((int)(dificilImg.width*FATOR_DIFF), (int)(dificilImg.height*FATOR_DIFF));
}



// as fun\u00e7\u00f5es abaixo funcionam de forma similar
public void loadBarImgs()
{
  playBtnImg = loadImage("btn_tocar.png");
  repeatBtnImg = loadImage("btn_repetir.png");
  backBtnImg = loadImage("btn_back.png");
  fwdBtnImg = loadImage("btn_fwd.png");
  
  float FATOR_BTN = sketchHeight()*0.09f/playBtnImg.height;
  
  playBtnImg.resize((int)(playBtnImg.width*FATOR_BTN), (int)(playBtnImg.height*FATOR_BTN));
  repeatBtnImg.resize((int)(repeatBtnImg.width*FATOR_BTN), (int)(repeatBtnImg.height*FATOR_BTN));
  backBtnImg.resize((int)(backBtnImg.width*FATOR_BTN), (int)(backBtnImg.height*FATOR_BTN));
  fwdBtnImg.resize((int)(fwdBtnImg.width*FATOR_BTN), (int)(fwdBtnImg.height*FATOR_BTN));
}



public void loadArrows()
{
  upArrowImg = loadImage("seta_sobe.png");
  downArrowImg = loadImage("seta_desce.png");
  rightArrowImg = loadImage("seta_mesmolugar.png");
  greenUpArrowImg = loadImage("seta_sobe_verde.png");
  greenDownArrowImg = loadImage("seta_desce_verde.png");
  greenRightArrowImg = loadImage("seta_mesmolugar_verde.png");
  redUpArrowImg = loadImage("seta_sobe_vermelho.png");
  redDownArrowImg = loadImage("seta_desce_vermelho.png");
  redRightArrowImg = loadImage("seta_mesmolugar_vermelho.png");
  
  float FATOR_SETA = sketchHeight()*0.44f/upArrowImg.height;
  
  upArrowImg.resize((int)(upArrowImg.width*FATOR_SETA), (int)(upArrowImg.height*FATOR_SETA));
  greenUpArrowImg.resize((int)(greenUpArrowImg.width*FATOR_SETA), (int)(greenUpArrowImg.height*FATOR_SETA));
  redUpArrowImg.resize((int)(redUpArrowImg.width*FATOR_SETA), (int)(redUpArrowImg.height*FATOR_SETA));
  downArrowImg.resize((int)(downArrowImg.width*FATOR_SETA), (int)(downArrowImg.height*FATOR_SETA));
  greenDownArrowImg.resize((int)(greenDownArrowImg.width*FATOR_SETA), (int)(greenDownArrowImg.height*FATOR_SETA));
  redDownArrowImg.resize((int)(redDownArrowImg.width*FATOR_SETA), (int)(redDownArrowImg.height*FATOR_SETA));
  rightArrowImg.resize((int)(rightArrowImg.width*FATOR_SETA), (int)(rightArrowImg.height*FATOR_SETA));
  greenRightArrowImg.resize((int)(greenRightArrowImg.width*FATOR_SETA), (int)(greenRightArrowImg.height*FATOR_SETA));  
  redRightArrowImg.resize((int)(redRightArrowImg.width*FATOR_SETA), (int)(redRightArrowImg.height*FATOR_SETA));
}



public void loadEasyImgs()
{
  easyBgImg = loadImage("bg_easy.png");
  guitarImg = loadImage("img_guitar.png");
  vocalImg = loadImage("img_vocal.png");
  
  final float FATOR_PESSOAS = sketchHeight()*0.85f/guitarImg.height;
  
  easyBgImg.resize(sketchWidth(), sketchHeight());
  
  guitarImg.resize((int)(guitarImg.width*FATOR_PESSOAS), (int)(guitarImg.height*FATOR_PESSOAS));
  vocalImg.resize((int)(vocalImg.width*FATOR_PESSOAS), (int)(vocalImg.height*FATOR_PESSOAS));
}



public void loadMediumImgs()
{
  mediumBgImg = loadImage("bg_medium.png");
  bateraImg = loadImage("img_batera.png");
  
  final float FATOR_BATERA = sketchHeight()*0.82f/bateraImg.height;
  
  mediumBgImg.resize(sketchWidth(), sketchHeight());
  
  bateraImg.resize((int)(bateraImg.width*FATOR_BATERA), (int)(bateraImg.height*FATOR_BATERA));
}



public void loadHardImgs()
{
  hardBgImg = loadImage("bg_hard.png");
  violinImg = loadImage("img_violino.png");
  violaoImg = loadImage("img_violao.png");
  
  final float FATOR_VIOLIN = sketchHeight()*0.82f/violinImg.height;
  final float FATOR_VIOLAO = sketchHeight()*0.66f/violaoImg.height;
  
  hardBgImg.resize(sketchWidth(), sketchHeight());
  
  violinImg.resize((int)(violinImg.width*FATOR_VIOLIN), (int)(violinImg.height*FATOR_VIOLIN));
  violaoImg.resize((int)(violaoImg.width*FATOR_VIOLAO), (int)(violaoImg.height*FATOR_VIOLAO));
}



public void loadEndImgs()
{
  endBgImg = loadImage("bg_end.jpg");
  txtPontuacaoImg = loadImage("img_pontuacao.png");
  
  final float FATOR_TEXTO = sketchWidth()*0.8f/txtPontuacaoImg.width; 
  
  endBgImg.resize(sketchWidth(), sketchHeight());
  txtPontuacaoImg.resize((int)(txtPontuacaoImg.width*FATOR_TEXTO), (int)(txtPontuacaoImg.height*FATOR_TEXTO)); 
}
public void difficultySelect()
{
  image(diffBgImg, 0, 0);
  image(galeraImg, 0, sketchHeight()-galeraImg.height);
  image(etiquetaImg, sketchWidth()/2-etiquetaImg.width/2, 0); // centralizando a etiqueta
  
  image(facilImg, sketchWidth()/2-facilImg.width/2, 3*sketchHeight()/13-facilImg.height/2);
  image(medioImg, sketchWidth()/2-medioImg.width/2, 5*sketchHeight()/13-medioImg.height/2);
  image(dificilImg, sketchWidth()/2-dificilImg.width/2, 7*sketchHeight()/13-dificilImg.height/2);
  
  // inicializa os bot\u00f5es de dificuldade (para que seja poss\u00edvel clicar neles)
  initDiffButtons();
  drawButtonArray(diffButtons);
  loaded = true;
}



public void easyMode()
{
  image(easyBgImg, 0, 0); // desenha a imagem de fundo relativa \u00e0 dificuldade
  image(guitarImg, sketchWidth()/30, sketchHeight()-guitarImg.height-sketchHeight()/64); // e os "bonecos" respectivos
  image(vocalImg, (int)(sketchWidth()*0.64f), sketchHeight()-vocalImg.height-sketchHeight()/128);
  
  tempo = getTempo(dificuldade); // inicializa a velocidade das notas relativa \u00e0 dificuldade

  drawPlayingInterface(); // desenha os componentes de jogo (setas, barra inferior, score)
  gameCode(); // c\u00f3digo que faz o jogo funcionar
}



// mediumMode() e hardMode() funcionam de maneira praticamente id\u00eantica ao easyMode()
public void mediumMode()
{
  image(mediumBgImg, 0, 0);
  image(bateraImg, sketchWidth()/25, sketchHeight()-bateraImg.height-sketchHeight()/64);
  
  tempo = getTempo(dificuldade);

  drawPlayingInterface();
  gameCode();
}



public void hardMode()
{
  image(hardBgImg, 0, 0);
  image(violinImg, (int)(sketchWidth()*0.18f), (int)(sketchHeight()*0.16f));
  image(violaoImg, 6*sketchWidth()/10, sketchHeight()/3);
  
  tempo = getTempo(dificuldade);

  drawPlayingInterface();
  gameCode();
}



// tela final
public void endScreen()
{
  image(endBgImg, 0, 0);
  image(txtPontuacaoImg, sketchWidth()/2-txtPontuacaoImg.width/2, 2*sketchHeight()/7); 
  
  // desenha o c\u00edrculo verde com o score dentro
  fill(goodColor);
  ellipse(sketchWidth()/2, 5*sketchHeight()/7, sketchHeight()/3, sketchHeight()/3);
  fill(whiteColor);
  textAlign(CENTER, CENTER);
  textSize(sketchHeight()/7);
  text(score, sketchWidth()/2, 5*sketchHeight()/7);  
}
class RectButton
{
  // vari\u00e1veis de cada bot\u00e3o
  int x, y, w, h, id;
  int c;
  
  // construtor
  public RectButton(int x, int y, int w, int h, int c, int id)
  {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.c = c;
    this.id = id;
  }
  
  // m\u00e9todo que verifica se o mouse est\u00e1 em cima do bot\u00e3o (e, portanto, o bot\u00e3o \u00e9 clic\u00e1vel)
  public boolean mouseOver()
  {
    if (mouseX >= x && mouseX <= x+w && mouseY >= y && mouseY <= y+h)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  // este m\u00e9todo, que torna o bot\u00e3o vis\u00edvel, s\u00f3 \u00e9 executado caso a vari\u00e1vel de debug BUTTON_HITBOXES = true
  public void drawArea()
  {
    noStroke();
    fill(c);
    rect(x, y, w, h);
  }
  
  // executa a\u00e7\u00f5es ao se clicar em um bot\u00e3o
  public void action()
  {
    boolean clickable = mouseOver();
    if (clickable) // se algum bot\u00e3o \u00e9 clic\u00e1vel
    {
      switch(id) // verifica qual foi e executa a a\u00e7\u00e3o correspondente
      {
        case FACIL: score = 0; arrowState = INI; dificuldade = FACIL; tela = JOGO_FACIL; break;
        case MEDIO: score = 0; arrowState = INI; dificuldade = MEDIO; tela = JOGO_MEDIO; break;
        case DIFICIL: score = 0; arrowState = INI; dificuldade = DIFICIL; tela = JOGO_DIFICIL; break;
        
        case PLAY: if (!playing) notas = playSound(N_NOTAS, tempo); break;
        case REPLAY: if (!playing) replaySound(notas, tempo); break;
        case BACK: if (!playing) tela = DIFF_SELECT; break;
        case FORWARD: if (!playing) tela = END_SCREEN; break; 
      }
    }
  }
}



// inicializa os bot\u00f5es de dificuldade (f\u00e1cil, m\u00e9dio, dif\u00edcil)
public void initDiffButtons()
{
  diffButtons = new RectButton[3];
  
  int diffButtonHeight = sketchHeight()/9;
  int diffButtonWidth = (int)(2.7f*diffButtonHeight);
  
  diffButtons[0] = new RectButton(sketchWidth()/2-diffButtonWidth/2, 3*sketchHeight()/13-diffButtonHeight/2, diffButtonWidth, diffButtonHeight, buttonHitboxColor, FACIL);
  diffButtons[1] = new RectButton(sketchWidth()/2-diffButtonWidth/2, 5*sketchHeight()/13-diffButtonHeight/2, diffButtonWidth, diffButtonHeight, buttonHitboxColor, MEDIO);
  diffButtons[2] = new RectButton(sketchWidth()/2-diffButtonWidth/2, 7*sketchHeight()/13-diffButtonHeight/2, diffButtonWidth, diffButtonHeight, buttonHitboxColor, DIFICIL);
}



// inicializa os bot\u00f5es da barra inferior (back, play, repeat, forward)
public void initBarButtons()
{
  barButtons = new RectButton[4];
  
  int barButtonHeight = sketchHeight()/10;
  int barButtonWidth = (int)(1.05f*barButtonHeight);
  
  barButtons[0] = new RectButton(15*sketchWidth()/32-barButtonWidth/2, (yTopo+sketchHeight())/2-barButtonHeight/2, barButtonWidth, barButtonHeight, buttonHitboxColor, PLAY);
  barButtons[1] = new RectButton(17*sketchWidth()/32-barButtonWidth/2, (yTopo+sketchHeight())/2-barButtonHeight/2, barButtonWidth, barButtonHeight, buttonHitboxColor, REPLAY);
  barButtons[2] = new RectButton(13*sketchWidth()/32-barButtonWidth/2, (yTopo+sketchHeight())/2-barButtonHeight/2, barButtonWidth, barButtonHeight, buttonHitboxColor, BACK);
  barButtons[3] = new RectButton(19*sketchWidth()/32-barButtonWidth/2, (yTopo+sketchHeight())/2-barButtonHeight/2, barButtonWidth, barButtonHeight, buttonHitboxColor, FORWARD);
}



// desenha as \u00e1reas dos bot\u00f5es, caso a vari\u00e1vel de debug BUTTON_HITBOXES = true
public void drawButtonArray(RectButton[] btns)
{
  if (BUTTON_HITBOXES)
  {
    for(int i = 0; i < btns.length; i++)
    {
      btns[i].drawArea();
    }
  }
}
// m\u00e9todo que processa a resposta enviada pelo usu\u00e1rio ao pressionar uma das teclas (up, down, right)
public void enviaResposta(int r)
{
  if (podeResponder) // se o usu\u00e1rio ainda n\u00e3o enviou uma resposta para a posi\u00e7\u00e3o atual
  {
    int correta = notas[posResposta+1] - notas[posResposta]; // calcula qual a resposta correta
    
    if (correta == -1)
    {
      if (r == DESCEU)
      {
        respostaCerta(INI+r);
      }
      else
      {
        respostaErrada(INI-r);
      }
    }
    else if (correta == 1)
    {
      if (r == SUBIU)
      {
        respostaCerta(INI+r);
      }
      else
      {
        respostaErrada(INI-r);
      }
    }
    else if (correta == 0)
    {
      if (r == IGUAL)
      {
        respostaCerta(INI+r);
      }
      else
      {
        respostaErrada(INI-r);
      }
    }
    
    // n\u00e3o pode mais enviar uma resposta para esta posi\u00e7\u00e3o
    jaRespondeu = true;
    podeResponder = false;
  }
}



// a\u00e7\u00f5es a executar quando o usu\u00e1rio acerta uma resposta
public void respostaCerta(int res)
{
  arrowState = res; // colore de verde a seta correspondente \u00e0 resposta certa
  score += 10; // aumenta o score
  posResposta++; // incrementar a posi\u00e7\u00e3o da pr\u00f3xima resposta
  verificaPos();
}



// a\u00e7\u00f5es a executar quando o usu\u00e1rio erra uma resposta (parecido com o m\u00e9todo anterior)
public void respostaErrada(int res)
{
  // se entrou nesse switch-case, \u00e9 porque o usu\u00e1rio n\u00e3o respondeu a tempo. colorir de vermelho a seta que seria a resposta correta
  switch(res)
  {
    case -1: res = DOWN_W; break;
    case +1: res = UP_W; break;
    case +0: res = RIGHT_W; break;
  }
  
  arrowState = res;
  score -= 5;
  posResposta++;
  verificaPos();
}



public void verificaPos() // m\u00e9todo que tenta evitar acessos \u00e0 morte, que travam o programa :(
{
  if (posResposta >= notas.length-1)
  {
    posResposta = 0;
    playing = false;
  }
}
// sorteia uma sequ\u00eancia de n notas e executa
public int[] playSound(int n, float tempo)
{
  out.pauseNotes();
  
  int[] notas = new int[n];
  
  // inicializa a primeira posi\u00e7\u00e3o
  notas[0] = PApplet.parseInt(random(0, freqs.length));
  
  out.playNote(0.0f, tempo-0.1f, freqs[notas[0]]);
  
  for(int i = 1; i < n; i++)
  {
      // caso especial de frequ\u00eancia mais baixa (n\u00e3o pode sortear uma nota mais baixa)
      if (notas[i-1] == 0)
      {
        notas[i] = notas[i-1] + PApplet.parseInt(random(2));
      }
      // caso especial de frequ\u00eancia mais alta (n\u00e3o pode sortear uma nota mais alta)
      else if (notas[i-1] == freqs.length-1)
      {
        notas[i] = notas[i-1] + PApplet.parseInt(random(2))-1;
      }
      // caso padr\u00e3o (sorteia frequ\u00eancia -1, +1 ou igual)
      else
      {
        notas[i] = notas[i-1] + PApplet.parseInt(random(3))-1;
      }
      
      out.playNote(tempo*i, tempo-0.1f, freqs[notas[i]]);
  }
  
  playing = true;
  score = SCORE_INICIAL; // reseta o score
  millisInicial = millis(); // armazena o momento em que come\u00e7ou a tocar as notas, para poder conferir se as respostas foram fornecidas a tempo
  out.resumeNotes(); // executa as notas
  
  return notas;
}



public void replaySound(int[] notas, float tempo) // repete a sequ\u00eancia sorteada anteriormente
{
  out.pauseNotes();
  
  for (int i = 0; i < notas.length; i++)
  {
    out.playNote(tempo*i, tempo-0.1f, freqs[notas[i]]); 
  }
  
  playing = true;
  score = SCORE_INICIAL;
  millisInicial = millis();
  out.resumeNotes();
}
/* declara\u00e7\u00f5es das vari\u00e1veis */



// tempo entre notas para cada dificuldade
static final float TEMPO_EASY = 2;
static final float TEMPO_MEDIUM = 1.2f;
static final float TEMPO_HARD = 0.6f;



// vari\u00e1veis do programa
static final int N_NOTAS = 15;              // n\u00famero de notas a ser executada em um jogo
static final int SCORE_INICIAL = 5*N_NOTAS; // score base ao iniciar um jogo. esse valor n\u00e3o permite que o usu\u00e1rio termine com score negativo, j\u00e1 que um erro custa 5 pontos
int tela = DIFF_SELECT;                     // tela a ser mostrada para o usu\u00e1rio
int dificuldade = -1;                       // dificuldade (f\u00e1cil, m\u00e9dio ou dif\u00edcil)
float tempo = -1;                           // tempo relacionado \u00e0 dificuldade
int millisInicial = MAX_INT;                // vari\u00e1vel que armazena o momento em que o usu\u00e1rio come\u00e7ou a responder uma sequ\u00eancia, usada para verificar se ele est\u00e1 respondendo a tempo
int arrowState = INI;                       // determina quais cores as flechas devem ter
int score = 0;                              // score do usu\u00e1rio
boolean loaded = false;                     // determina quando o programa deve come\u00e7ar a aceitar cliques do usu\u00e1rio
boolean podeResponder = false;              // determina se o usu\u00e1rio pode ou n\u00e3o enviar uma resposta
boolean jaRespondeu = false;                // determina se o usu\u00e1rio j\u00e1 enviou uma resposta para a nota atual
boolean playing = false;                    // determina se o usu\u00e1rio est\u00e1 jogando no momento
int posResposta = 0;                        // posi\u00e7\u00e3o atual sendo respondida (por exemplo, se 0, o usu\u00e1rio deve enviar a resposta relativa \u00e0s posi\u00e7\u00f5es 0 e 1)
int goodColor = color(180, 214, 96);      // cor relativa \u00e0s coisas corretas
int badColor = color(240, 80, 80);        // cor relativa \u00e0s coisas erradas
int whiteColor = color(248, 248, 255);    // branco legal
int yTopo;                                  // posi\u00e7\u00e3o Y do topo da barra
int[] notas;                                // vetor com as notas que s\u00e3o executadas em uma sequ\u00eancia



// vetores de bot\u00f5es
RectButton[] barButtons;
RectButton[] diffButtons;



// vari\u00e1veis para testes das hitboxes dos bot\u00f5es
static final boolean BUTTON_HITBOXES = false; // mostra ou n\u00e3o as hitboxes
int buttonHitboxColor = color(192, 0, 192, 128);



// frequ\u00eancias de notas que podem ser executadas
String[] freqs = {"A4", "B4", "C#5", "D5", "E5", "F#5", "G#5", "A5", "B5", "C#6", "E6"};



// objetos minim
Minim minim;
AudioOutput out;



// vari\u00e1veis relacionadas \u00e0 interface
PImage bg, loadingImg;



// imagens
PImage diffBgImg, galeraImg, etiquetaImg, facilImg, medioImg, dificilImg; // tela de escolher dificuldade
PImage upArrowImg, downArrowImg, rightArrowImg, greenUpArrowImg, greenDownArrowImg, greenRightArrowImg, redUpArrowImg, redDownArrowImg, redRightArrowImg; // setas de resposta
PImage playBtnImg, repeatBtnImg, backBtnImg, fwdBtnImg; // bot\u00f5es da barra inferior
PImage easyBgImg, guitarImg, vocalImg; // imagens do easy mode
PImage mediumBgImg, bateraImg; // imagens do medium mode
PImage hardBgImg, violinImg, violaoImg; // imagens do hard mode
PImage endBgImg, txtPontuacaoImg; // imagens da tela final, onde o score \u00e9 mostrado



/* abaixo grupos de constantes */

// tela em que o usu\u00e1rio est\u00e1
static final int DIFF_SELECT = 100;
static final int JOGO_FACIL = 101;
static final int JOGO_MEDIO = 102;
static final int JOGO_DIFICIL = 103;
static final int END_SCREEN = 104;

// dificuldades
static final int FACIL = 200;
static final int MEDIO = 201;
static final int DIFICIL = 202;

// bot\u00f5es da barra inferior
static final int PLAY = 300;
static final int REPLAY = 301;
static final int BACK = 302;
static final int FORWARD = 303;

// constantes relacionadas \u00e0s respostas
static final int DESCEU = 1000;
static final int SUBIU = 2000;
static final int IGUAL = 3000;

// constantes relativas ao estado das flechas
static final int INI = 10000;
static final int DOWN_R = INI+DESCEU;
static final int UP_R = INI+SUBIU;
static final int RIGHT_R = INI+IGUAL;
static final int DOWN_W = INI-DESCEU;
static final int UP_W = INI-SUBIU;
static final int RIGHT_W = INI-IGUAL;



// isso existe no lugar do size() e define o tamanho da janela como o tamanho da tela do usu\u00e1rio
public int sketchWidth()  { return displayWidth; }
public int sketchHeight() { return displayHeight; }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--hide-stop", "ListenUp" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
