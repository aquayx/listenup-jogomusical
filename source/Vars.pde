/* declarações das variáveis */



// tempo entre notas para cada dificuldade
static final float TEMPO_EASY = 2;
static final float TEMPO_MEDIUM = 1.2;
static final float TEMPO_HARD = 0.6;



// variáveis do programa
static final int N_NOTAS = 15;              // número de notas a ser executada em um jogo
static final int SCORE_INICIAL = 5*N_NOTAS; // score base ao iniciar um jogo. esse valor não permite que o usuário termine com score negativo, já que um erro custa 5 pontos
int tela = DIFF_SELECT;                     // tela a ser mostrada para o usuário
int dificuldade = -1;                       // dificuldade (fácil, médio ou difícil)
float tempo = -1;                           // tempo relacionado à dificuldade
int millisInicial = MAX_INT;                // variável que armazena o momento em que o usuário começou a responder uma sequência, usada para verificar se ele está respondendo a tempo
int arrowState = INI;                       // determina quais cores as flechas devem ter
int score = 0;                              // score do usuário
boolean loaded = false;                     // determina quando o programa deve começar a aceitar cliques do usuário
boolean podeResponder = false;              // determina se o usuário pode ou não enviar uma resposta
boolean jaRespondeu = false;                // determina se o usuário já enviou uma resposta para a nota atual
boolean playing = false;                    // determina se o usuário está jogando no momento
int posResposta = 0;                        // posição atual sendo respondida (por exemplo, se 0, o usuário deve enviar a resposta relativa às posições 0 e 1)
color goodColor = color(180, 214, 96);      // cor relativa às coisas corretas
color badColor = color(240, 80, 80);        // cor relativa às coisas erradas
color whiteColor = color(248, 248, 255);    // branco legal
int yTopo;                                  // posição Y do topo da barra
int[] notas;                                // vetor com as notas que são executadas em uma sequência



// vetores de botões
RectButton[] barButtons;
RectButton[] diffButtons;



// variáveis para testes das hitboxes dos botões
static final boolean BUTTON_HITBOXES = false; // mostra ou não as hitboxes
color buttonHitboxColor = color(192, 0, 192, 128);



// frequências de notas que podem ser executadas
String[] freqs = {"A4", "B4", "C#5", "D5", "E5", "F#5", "G#5", "A5", "B5", "C#6", "E6"};



// objetos minim
Minim minim;
AudioOutput out;



// variáveis relacionadas à interface
PImage bg, loadingImg;



// imagens
PImage diffBgImg, galeraImg, etiquetaImg, facilImg, medioImg, dificilImg; // tela de escolher dificuldade
PImage upArrowImg, downArrowImg, rightArrowImg, greenUpArrowImg, greenDownArrowImg, greenRightArrowImg, redUpArrowImg, redDownArrowImg, redRightArrowImg; // setas de resposta
PImage playBtnImg, repeatBtnImg, backBtnImg, fwdBtnImg; // botões da barra inferior
PImage easyBgImg, guitarImg, vocalImg; // imagens do easy mode
PImage mediumBgImg, bateraImg; // imagens do medium mode
PImage hardBgImg, violinImg, violaoImg; // imagens do hard mode
PImage endBgImg, txtPontuacaoImg; // imagens da tela final, onde o score é mostrado



/* abaixo grupos de constantes */

// tela em que o usuário está
static final int DIFF_SELECT = 100;
static final int JOGO_FACIL = 101;
static final int JOGO_MEDIO = 102;
static final int JOGO_DIFICIL = 103;
static final int END_SCREEN = 104;

// dificuldades
static final int FACIL = 200;
static final int MEDIO = 201;
static final int DIFICIL = 202;

// botões da barra inferior
static final int PLAY = 300;
static final int REPLAY = 301;
static final int BACK = 302;
static final int FORWARD = 303;

// constantes relacionadas às respostas
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



// isso existe no lugar do size() e define o tamanho da janela como o tamanho da tela do usuário
public int sketchWidth()  { return displayWidth; }
public int sketchHeight() { return displayHeight; }
