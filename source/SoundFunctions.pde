// sorteia uma sequência de n notas e executa
int[] playSound(int n, float tempo)
{
  out.pauseNotes();
  
  int[] notas = new int[n];
  
  // inicializa a primeira posição
  notas[0] = int(random(0, freqs.length));
  
  out.playNote(0.0, tempo-0.1, freqs[notas[0]]);
  
  for(int i = 1; i < n; i++)
  {
      // caso especial de frequência mais baixa (não pode sortear uma nota mais baixa)
      if (notas[i-1] == 0)
      {
        notas[i] = notas[i-1] + int(random(2));
      }
      // caso especial de frequência mais alta (não pode sortear uma nota mais alta)
      else if (notas[i-1] == freqs.length-1)
      {
        notas[i] = notas[i-1] + int(random(2))-1;
      }
      // caso padrão (sorteia frequência -1, +1 ou igual)
      else
      {
        notas[i] = notas[i-1] + int(random(3))-1;
      }
      
      out.playNote(tempo*i, tempo-0.1, freqs[notas[i]]);
  }
  
  playing = true;
  score = SCORE_INICIAL; // reseta o score
  millisInicial = millis(); // armazena o momento em que começou a tocar as notas, para poder conferir se as respostas foram fornecidas a tempo
  out.resumeNotes(); // executa as notas
  
  return notas;
}



void replaySound(int[] notas, float tempo) // repete a sequência sorteada anteriormente
{
  out.pauseNotes();
  
  for (int i = 0; i < notas.length; i++)
  {
    out.playNote(tempo*i, tempo-0.1, freqs[notas[i]]); 
  }
  
  playing = true;
  score = SCORE_INICIAL;
  millisInicial = millis();
  out.resumeNotes();
}
