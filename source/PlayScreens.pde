void difficultySelect()
{
  image(diffBgImg, 0, 0);
  image(galeraImg, 0, sketchHeight()-galeraImg.height);
  image(etiquetaImg, sketchWidth()/2-etiquetaImg.width/2, 0); // centralizando a etiqueta
  
  image(facilImg, sketchWidth()/2-facilImg.width/2, 3*sketchHeight()/13-facilImg.height/2);
  image(medioImg, sketchWidth()/2-medioImg.width/2, 5*sketchHeight()/13-medioImg.height/2);
  image(dificilImg, sketchWidth()/2-dificilImg.width/2, 7*sketchHeight()/13-dificilImg.height/2);
  
  // inicializa os botões de dificuldade (para que seja possível clicar neles)
  initDiffButtons();
  drawButtonArray(diffButtons);
  loaded = true;
}



void easyMode()
{
  image(easyBgImg, 0, 0); // desenha a imagem de fundo relativa à dificuldade
  image(guitarImg, sketchWidth()/30, sketchHeight()-guitarImg.height-sketchHeight()/64); // e os "bonecos" respectivos
  image(vocalImg, (int)(sketchWidth()*0.64), sketchHeight()-vocalImg.height-sketchHeight()/128);
  
  tempo = getTempo(dificuldade); // inicializa a velocidade das notas relativa à dificuldade

  drawPlayingInterface(); // desenha os componentes de jogo (setas, barra inferior, score)
  gameCode(); // código que faz o jogo funcionar
}



// mediumMode() e hardMode() funcionam de maneira praticamente idêntica ao easyMode()
void mediumMode()
{
  image(mediumBgImg, 0, 0);
  image(bateraImg, sketchWidth()/25, sketchHeight()-bateraImg.height-sketchHeight()/64);
  
  tempo = getTempo(dificuldade);

  drawPlayingInterface();
  gameCode();
}



void hardMode()
{
  image(hardBgImg, 0, 0);
  image(violinImg, (int)(sketchWidth()*0.18), (int)(sketchHeight()*0.16));
  image(violaoImg, 6*sketchWidth()/10, sketchHeight()/3);
  
  tempo = getTempo(dificuldade);

  drawPlayingInterface();
  gameCode();
}



// tela final
void endScreen()
{
  image(endBgImg, 0, 0);
  image(txtPontuacaoImg, sketchWidth()/2-txtPontuacaoImg.width/2, 2*sketchHeight()/7); 
  
  // desenha o círculo verde com o score dentro
  fill(goodColor);
  ellipse(sketchWidth()/2, 5*sketchHeight()/7, sketchHeight()/3, sketchHeight()/3);
  fill(whiteColor);
  textAlign(CENTER, CENTER);
  textSize(sketchHeight()/7);
  text(score, sketchWidth()/2, 5*sketchHeight()/7);  
}
