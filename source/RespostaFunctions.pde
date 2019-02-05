// método que processa a resposta enviada pelo usuário ao pressionar uma das teclas (up, down, right)
void enviaResposta(int r)
{
  if (podeResponder) // se o usuário ainda não enviou uma resposta para a posição atual
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
    
    // não pode mais enviar uma resposta para esta posição
    jaRespondeu = true;
    podeResponder = false;
  }
}



// ações a executar quando o usuário acerta uma resposta
void respostaCerta(int res)
{
  arrowState = res; // colore de verde a seta correspondente à resposta certa
  score += 10; // aumenta o score
  posResposta++; // incrementar a posição da próxima resposta
  verificaPos();
}



// ações a executar quando o usuário erra uma resposta (parecido com o método anterior)
void respostaErrada(int res)
{
  // se entrou nesse switch-case, é porque o usuário não respondeu a tempo. colorir de vermelho a seta que seria a resposta correta
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



void verificaPos() // método que tenta evitar acessos à morte, que travam o programa :(
{
  if (posResposta >= notas.length-1)
  {
    posResposta = 0;
    playing = false;
  }
}
