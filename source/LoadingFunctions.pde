void loadingScreen()
{
  background(255,204,42);
  loadingImg = loadImage("img_abertura.png");
  
  // fator de redimensionamento da imagem
  final float FATOR = 0.8;
  
  // calcula a proporção em que a imagem de carregamento deve ser redimensionada
  int[] dimensoes = fitToScreen(loadingImg, 'h');

  // centraliza a imagem e redimensiona de acordo com o fator
  loadingImg.resize((int)(dimensoes[0]*FATOR), (int)(dimensoes[1]*FATOR));
  image(loadingImg, width/2-loadingImg.width/2, height/2-loadingImg.height/2); // centraliza
}



// inicializa e carrega os objetos
void load()
{
  // inicializa os objetos de áudio
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



/* abaixo, apenas funções que preparam imagens (carregam e redimensionam de acordo com as dimensões da tela) */

void loadDifficultyImgs()
{
  diffBgImg = loadImage("bg_menu.jpg");
  galeraImg = loadImage("img_galera.png");
  etiquetaImg = loadImage("img_etiqueta.png");
  facilImg = loadImage("dif_facil.png");
  medioImg = loadImage("dif_medio.png");
  dificilImg = loadImage("dif_dificil.png");
  
  // calcula um fator de redimensionamento para a etiqueta e as imagens de dificuldade
  // isso é necessário para que a interface seja apresentável em qualquer tamanho de tela
  final float FATOR_ETIQUETA = sketchWidth()*0.12/etiquetaImg.width;
  final float FATOR_DIFF = sketchHeight()*0.12/facilImg.height;

  // redimensiona o background para o tamanho da tela
  diffBgImg.resize(sketchWidth(), sketchHeight());

  // redimensiona a galera proporcionalmente à largura da tela
  int[] dimensoesGalera = fitToScreen(galeraImg, 'w');
  galeraImg.resize(dimensoesGalera[0], dimensoesGalera[1]);
  
  // redimensiona as imagens de acordo com o fator calculado
  etiquetaImg.resize((int)(etiquetaImg.width*FATOR_ETIQUETA), (int)(etiquetaImg.height*FATOR_ETIQUETA));
  facilImg.resize((int)(facilImg.width*FATOR_DIFF), (int)(facilImg.height*FATOR_DIFF));
  medioImg.resize((int)(medioImg.width*FATOR_DIFF), (int)(medioImg.height*FATOR_DIFF));
  dificilImg.resize((int)(dificilImg.width*FATOR_DIFF), (int)(dificilImg.height*FATOR_DIFF));
}



// as funções abaixo funcionam de forma similar
void loadBarImgs()
{
  playBtnImg = loadImage("btn_tocar.png");
  repeatBtnImg = loadImage("btn_repetir.png");
  backBtnImg = loadImage("btn_back.png");
  fwdBtnImg = loadImage("btn_fwd.png");
  
  float FATOR_BTN = sketchHeight()*0.09/playBtnImg.height;
  
  playBtnImg.resize((int)(playBtnImg.width*FATOR_BTN), (int)(playBtnImg.height*FATOR_BTN));
  repeatBtnImg.resize((int)(repeatBtnImg.width*FATOR_BTN), (int)(repeatBtnImg.height*FATOR_BTN));
  backBtnImg.resize((int)(backBtnImg.width*FATOR_BTN), (int)(backBtnImg.height*FATOR_BTN));
  fwdBtnImg.resize((int)(fwdBtnImg.width*FATOR_BTN), (int)(fwdBtnImg.height*FATOR_BTN));
}



void loadArrows()
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
  
  float FATOR_SETA = sketchHeight()*0.44/upArrowImg.height;
  
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



void loadEasyImgs()
{
  easyBgImg = loadImage("bg_easy.png");
  guitarImg = loadImage("img_guitar.png");
  vocalImg = loadImage("img_vocal.png");
  
  final float FATOR_PESSOAS = sketchHeight()*0.85/guitarImg.height;
  
  easyBgImg.resize(sketchWidth(), sketchHeight());
  
  guitarImg.resize((int)(guitarImg.width*FATOR_PESSOAS), (int)(guitarImg.height*FATOR_PESSOAS));
  vocalImg.resize((int)(vocalImg.width*FATOR_PESSOAS), (int)(vocalImg.height*FATOR_PESSOAS));
}



void loadMediumImgs()
{
  mediumBgImg = loadImage("bg_medium.png");
  bateraImg = loadImage("img_batera.png");
  
  final float FATOR_BATERA = sketchHeight()*0.82/bateraImg.height;
  
  mediumBgImg.resize(sketchWidth(), sketchHeight());
  
  bateraImg.resize((int)(bateraImg.width*FATOR_BATERA), (int)(bateraImg.height*FATOR_BATERA));
}



void loadHardImgs()
{
  hardBgImg = loadImage("bg_hard.png");
  violinImg = loadImage("img_violino.png");
  violaoImg = loadImage("img_violao.png");
  
  final float FATOR_VIOLIN = sketchHeight()*0.82/violinImg.height;
  final float FATOR_VIOLAO = sketchHeight()*0.66/violaoImg.height;
  
  hardBgImg.resize(sketchWidth(), sketchHeight());
  
  violinImg.resize((int)(violinImg.width*FATOR_VIOLIN), (int)(violinImg.height*FATOR_VIOLIN));
  violaoImg.resize((int)(violaoImg.width*FATOR_VIOLAO), (int)(violaoImg.height*FATOR_VIOLAO));
}



void loadEndImgs()
{
  endBgImg = loadImage("bg_end.jpg");
  txtPontuacaoImg = loadImage("img_pontuacao.png");
  
  final float FATOR_TEXTO = sketchWidth()*0.8/txtPontuacaoImg.width; 
  
  endBgImg.resize(sketchWidth(), sketchHeight());
  txtPontuacaoImg.resize((int)(txtPontuacaoImg.width*FATOR_TEXTO), (int)(txtPontuacaoImg.height*FATOR_TEXTO)); 
}
