Sistema inteligente para gerenciamento de elevadores prediais.
Daniel Paiva Fernades1, Yasmin Bacha Castro1
1Universidade Federal de Itajubá (UNIFEI).
{ daniel.paivafernandes@gmail.com, yasmin95bacha@hotmail.com }
Resumo. Esse artigo propõe um sistema inteligente para gerenciamento do tráfego de elevadores prediais. O principal objetivo é a comparação de um método comum e um método inteligente para o controle dos elevadores. Esse método inteligente por sua vez é um sistema de inferência fuzzy.
<br>
<b>1. INTRODUÇÃO</b>
<br>
Existe uma tendência crescente pela busca de soluções da vida moderna que demandem maior comodidade e ao mesmo tempo garanta sustentabilidade. O fator sustentabilidade de justifica não só pela necessidade preservação do meio ambiente natural ou artificial, mas também pela otimização no uso eficiente recursos naturais, dentre os quais o aproveitamento energético.
Prédios são responsáveis pelo consumo de 42% do total de eletricidade gerada no Brasil. Neste cenário, os edifícios comerciais utilizam 11% do consumo energético, enquanto que os residenciais consomem 23% e os prédios públicos 8% [NOYA et al 2013].
Em nosso país, existem em média 200.000 elevadores em operação. Considerando o consumo de um elevador médio de 10HP, o que equivale à 75 lâmpadas de 100W para fins de comparação, o funcionamento de todo sistema de elevação predial consome mais de um décimo da energia produzida pela hidrelétrica de Itaipú (INFOLEV).
Diante deste cenário, e considerando a necessidade de racionamento de energia elétrica e a importância de otimização de mecanismos que proporcionem conforto e comodidade de forma sustentável, necessário se faz o desenvolvimento de um sistema capaz de reduzir o consumo de energia elétrica e atender rapidamente aos usuários pela gestão inteligente dos serviços de elevação predial.
2. INFORMAÇÕES SOBRE O DOMÍNIO DO PROBLEMA
Para implementação do sistema convencional de elevadores, foram levantadas
as características do funcionamento do elevador predial definido como modelo. Para
tanto, foi definido que o sistema modelo simularia um prédio de 10 (dez) andares com 2
(dois) elevadores.
Um elevador predial residencial opera, normalmente, à uma velocidade média
de 10 m/s. Considerando que existe ainda a variação de aceleração e frenagem quando
transita de um estado (parado) para outro (em movimento), para fins de simulação foi
adotada a velocidade média de 1 m/s. Foi considerado ainda que cada andar possui 3
metros de altura.
Há ainda o tempo entre abertura e fechamento das portas. O tempo médio para
abertura das portas, passagem do usuário para entrar ou sair e fechamento das portas é
de em média 10 segundos.
3. MODELAGEM E IMPLEMENTAÇÃO DO SISTEMA
3.1. IMPLEMENTAÇÃO DO MODELO CONVENCIONAL
Para implementação do projeto, foi utilizada a IDE Eclipse. A linguagem
adotada foi o Java. O código completo do projeto pode ser analisado pelo ANEXO I que
acompanha o presente relatório.
Após a implementação do código, o projeto foi compilado para testes da
Interface Gráfica do Usuário. O resultado pode ser constatado pelas imagens abaixo:
Figura 1 - Elevador 1 parado e Elevador 2 descendo Figura 2 - Elevador 1 parado e Elevador 2 chegou ao destino
Os elevadores são representados pelos quadrados coloridos. Enquanto parados
sua coloração é verde (ocioso), se em movimento sua coloração muda para amarelo (em
uso) e quando chegam ao seu destino sua coloração muda para azul (concluído). A
coluna do meio da interface gráfica representa os botões externos constantes em cada
servidão comum dos andares do prédio.
Quando o elevador é chamado pelo botão externo e chega à sua origem,
uma janela do simulador é aberta para representar a interface do painel de controle
interno do elevador, onde o usuário selecionará o andar de destino, conforme imagem à
seguir.
Figura 3 - Painel de controle interno do elevador
Para fins de automatização do teste do projeto, foi implementada uma função
para gerar chamadas aleatórias. A escolha do modo manual ou aleatório é feita no início
da execução do programa.
3.2. Implementação do banco de dados
Para viabilizar a contagem de tempo entre chamadas e reunir os dados
necessários para avaliação do desempenho do sistema convencional de elevadores
representado pelo simulador implementado, foi associado um banco de dados ao
projeto. O SBD adotado foi o PostegreSQL, versão 9.3. O modelo conceitual do banco
de dados construído pode ser representado pelo Diagrama de Entidade-Relacionamento
abaixo:
Figura 5 - Modelo ER do BD SimuladorElevador
3.3. RESULTADOS
Com base em toda construção apresentada, o sistema gerou 1.000 chamadas
randômicas, registrando o desempenho dos elevadores. Os dados extraídos foram
organizados numa tabela organizada em 5 atributos (Id, data, hora de início, hora final,
piso inicial, piso final e respectivo elevador) e 1.000 tuplas.
A execução do algoritmo gerou o total de XXXXXX horas de trabalho. O
quadro resumo abaixo representa o tempo mínimo, tempo máximo, média, mediana e
moda por quantidade de andares percorridos (tempo em formato HH:MM:SS). Estes
valores podem ser traduzidos pelo seguinte gráfico.
4. IMPLEMENTAÇÃO DO CONTROLE DO USANDO LÓGICA
FUZZY
Uma vez construído o algoritmo convencional, e com base nos resultados
extraídos da simulação, a próxima etapa foi o aproveitamento da estrutura do projeto
para criação do sistema especialista baseado em lógica fuzzy.
Tendo em vista que o projeto foi construído utilizando a linguagem JAVA, foi
utilizado a biblioteca de sistemas fuzzy “JFuzzyLogic” para otimização dos resultados.
Basicamente a aplicação disponível em <http://jfuzzylogic.sourceforge.net>, teve sua
biblioteca importada ao projeto original.
Esta ferramenta possui uma interface gráfica que permite a visualização dos
gráficos de entrada que serão tratados pelo sistema fuzzy para que sejam geradas as
saídas do simulador.
Cabe destacar, porém, que na lógica clássica, uma sentença somente seria
inferida caso todas as suas condições fossem verdadeiras. Isto ocorre de forma diferente
nos sistemas de inferência fuzzy, pois as premissas assumem graus de verdade. Isto se
reflete no resultado do processamento da sentença. Portanto, quanto maiores os graus de
00:00:00
00:00:43
00:01:26
00:02:10
00:02:53
1 2 3 4 5 6 7 8
Algoritmo convencional do elevador
Mínimo Máximo Média Mediana Modo
inclusão das variáveis de entrada, maior será o grau de inclusão da variável de saída no
conjunto correspondente.
O método de defuzzificação utilizado é o método do centro de gravidade: é o
método mais utilizado, e se baseia no cálculo do centro de gravidade da função de
associação. No método do centro de gravidade, calcula-se a área da curva da variável
lingüistica de saída produzida pela máquina de inferência, e acha-se o índice
correspondente que divide esta área a metade.
O gráfico que represente os resultados da execução destes é o seguinte:
5. COMPARATIVO
Com base nos resultados de ambas simulações, foi possível desenvolver uma
série de gráficos que demonstram a performance entre um e outro.
6. CONCLUSÕES
Uma vez cruzados os dados, concluímos que:
O tempo mínimo nos dois cenários, como de se esperar, não possuem
diferenças sensíveis, uma vez que o no melhor caso, não havendo concorrência em
00:00:00
00:00:43
00:01:26
00:02:10
1 2 3 4 5 6 7 8
Algoritmo Fuzzy do elevador
Mínimo Máximo Média Mediana Modo
09:11:01
11:01:36
00:00:00
02:24:00
04:48:00
07:12:00
09:36:00
12:00:00
Tempo de Operação
Fuzzy Convencional
qualquer das situações, o tempo mínimo é a abertura e fechamento das portas do
elevador acrescido de três segundos por andar percorrido.
Quanto ao tempo máximo de espera da chamada, o algoritmo que usa o método
inteligente demonstrou ser, em média, 23,31% mais eficiente o algoritmo convencional.
O tempo médio de espera do algoritmo inteligente é em média 16,21% mais eficiente.
Enquanto o algoritmo convencional atendeu as chamadas simuladas em
11h01m36s, o algoritmo inteligente executou a mesma tarefa em 09h11m01s. Ou seja,
houve uma economia de 01h50m35s, uma eficiência de 16,64% em relação ao
algoritmo convencional.
Para os dois elevadores 150 lâmpadas de 100W (calcular a economia de
consumo).
Segue o Quadro Resumo das conclusões apresentadas:
Andares
percorridos
Min
(%)
Max
(%)
Méd
(%)
1 0,00 17,86 10,00
2 0,00 21,74 16,13
3 0,00 21,88 17,07
4 4,35 23,46 21,15
5 0,00 25,25 21,88
6 0,00 26,27 16,18
7 0,00 24,06 13,51
8 6,98 25,97 13,79
MÉDIAS 1,41 23,31 16,21
Tabela 1 - Quadro Resumo de comparativo de eficência
7. REFERÊNCIAS
INFOLEV ELEVADORES & INFORMÁTICA LTDA “Consumo
de Energia Elétrica em Elevadores”. Disponível em <www.elevadores
ressi.com.br/energia.doc> Acessado em 16/03/2015.
NOYA, M., SEROA, A. e ABREU, W. (2013) “Eficiência Energética,
Sustentabilidade e Conforto Ambiental: Benefícios da Habitação Social Bioclimática”.
Rio de Janeiro: IX Congresso Nacional de Excelência em Gestão.
Siikonen, M.L., Leppala, J. (1991). “Elevator Traffic Pattern
Recognition.” In: Proccedings of 4th International Conference on Fuzzy
System Association, pp. 195-198, Brussels, Belgium.
