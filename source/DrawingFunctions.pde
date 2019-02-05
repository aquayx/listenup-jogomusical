void drawButtonBar()
{
  // calcula a posição Y do topo do retângulo transparente de acordo com o tamanho da janela
  yTopo = sketchHeight() - sketchHeight()/6;
  
  // desenha a linha branca
  stroke(255);
  line(0, yTopo, sketchWidth(), yTopo);
  
  // desenha o retângulo transparente
  noStroke();
  fill(255, 255, 255, 128);
  rect(0, yTopo, sketchWidth(), sketchHeight()-yTopo);
  
  // desenha os botões
  image(playBtnImg, 15*sketchWidth()/32-playBtnImg.width/2, (yTopo+sketchHeight())/2-playBtnImg.height/2);
  image(repeatBtnImg, 17*sketchWidth()/32-repeatBtnImg.width/2, (yTopo+sketchHeight())/2-repeatBtnImg.height/2);
  image(backBtnImg, 13*sketchWidth()/32-repeatBtnImg.width/2, (yTopo+sketchHeight())/2-repeatBtnImg.height/2);
  image(fwdBtnImg, 19*sketchWidth()/32-repeatBtnImg.width/2, (yTopo+sketchHeight())/2-repeatBtnImg.height/2);
}



// desenha as três flechas de acordo com o estado atual do jogo (estado inicial/houve um acerto/houve um erro)
void drawArrows()
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



void drawScore()
{
  // calcula o tamanho e posição dos componentes de score
  int rectWidth = (int)(sketchWidth()*0.13);
  int rectHeight = (int)(sketchHeight()*0.12);
  int rectXPos = sketchWidth() - rectWidth;
  int rectYPos = sketchHeight()/22;
  
  
  // desenha o retângulo verde
  fill(goodColor);  
  rect(rectXPos, rectYPos, rectWidth, rectHeight);
  
  // desenha o círculo branco (podemos usar os tamanhos que descobrimos para o rect, já que eles possuem relação entre si)
  fill(whiteColor);
  ellipse(sketchWidth()-rectWidth, sketchHeight()/22+rectHeight/2, rectHeight, rectHeight);
 
  // desenha o texto de score 
  textAlign(CENTER, CENTER);
  textSize(rectHeight/3);
  text(score, rectXPos + 3*rectWidth/5, rectYPos+rectHeight/2);
  
  // desenha texto de +10 ou -5 com a cor apropriada
  if (arrowState > INI) // método interessante de verificar se a última resposta enviada pelo usuário foi correta
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
