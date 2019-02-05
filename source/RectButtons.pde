class RectButton
{
  // variáveis de cada botão
  int x, y, w, h, id;
  color c;
  
  // construtor
  public RectButton(int x, int y, int w, int h, color c, int id)
  {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.c = c;
    this.id = id;
  }
  
  // método que verifica se o mouse está em cima do botão (e, portanto, o botão é clicável)
  boolean mouseOver()
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
  
  // este método, que torna o botão visível, só é executado caso a variável de debug BUTTON_HITBOXES = true
  void drawArea()
  {
    noStroke();
    fill(c);
    rect(x, y, w, h);
  }
  
  // executa ações ao se clicar em um botão
  void action()
  {
    boolean clickable = mouseOver();
    if (clickable) // se algum botão é clicável
    {
      switch(id) // verifica qual foi e executa a ação correspondente
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



// inicializa os botões de dificuldade (fácil, médio, difícil)
void initDiffButtons()
{
  diffButtons = new RectButton[3];
  
  int diffButtonHeight = sketchHeight()/9;
  int diffButtonWidth = (int)(2.7*diffButtonHeight);
  
  diffButtons[0] = new RectButton(sketchWidth()/2-diffButtonWidth/2, 3*sketchHeight()/13-diffButtonHeight/2, diffButtonWidth, diffButtonHeight, buttonHitboxColor, FACIL);
  diffButtons[1] = new RectButton(sketchWidth()/2-diffButtonWidth/2, 5*sketchHeight()/13-diffButtonHeight/2, diffButtonWidth, diffButtonHeight, buttonHitboxColor, MEDIO);
  diffButtons[2] = new RectButton(sketchWidth()/2-diffButtonWidth/2, 7*sketchHeight()/13-diffButtonHeight/2, diffButtonWidth, diffButtonHeight, buttonHitboxColor, DIFICIL);
}



// inicializa os botões da barra inferior (back, play, repeat, forward)
void initBarButtons()
{
  barButtons = new RectButton[4];
  
  int barButtonHeight = sketchHeight()/10;
  int barButtonWidth = (int)(1.05*barButtonHeight);
  
  barButtons[0] = new RectButton(15*sketchWidth()/32-barButtonWidth/2, (yTopo+sketchHeight())/2-barButtonHeight/2, barButtonWidth, barButtonHeight, buttonHitboxColor, PLAY);
  barButtons[1] = new RectButton(17*sketchWidth()/32-barButtonWidth/2, (yTopo+sketchHeight())/2-barButtonHeight/2, barButtonWidth, barButtonHeight, buttonHitboxColor, REPLAY);
  barButtons[2] = new RectButton(13*sketchWidth()/32-barButtonWidth/2, (yTopo+sketchHeight())/2-barButtonHeight/2, barButtonWidth, barButtonHeight, buttonHitboxColor, BACK);
  barButtons[3] = new RectButton(19*sketchWidth()/32-barButtonWidth/2, (yTopo+sketchHeight())/2-barButtonHeight/2, barButtonWidth, barButtonHeight, buttonHitboxColor, FORWARD);
}



// desenha as áreas dos botões, caso a variável de debug BUTTON_HITBOXES = true
void drawButtonArray(RectButton[] btns)
{
  if (BUTTON_HITBOXES)
  {
    for(int i = 0; i < btns.length; i++)
    {
      btns[i].drawArea();
    }
  }
}
