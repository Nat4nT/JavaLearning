// package AtividadeFinal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;

public class Transportadora implements ImportArquivos{
    float valorExpresso;
    float valorNormal;
    BufferedReader reader;
    int qtdAlert;

    int[] nroAlert = new int[1000];
    float[] pesoAlert = new float[1000];
    float[] freteAlert = new float[1000];


   private Encomenda[] vetEncomendas;
   private EncomendaExpressa[] vetEncomendasExpressas;
   private int qtdEncomenda = 0;
   private int qtdEncomendaExpressa = 0;


 
   public Transportadora(){
        this.vetEncomendas =  new Encomenda[1000];
        this.vetEncomendasExpressas = new EncomendaExpressa[1000];
   }
   
   public static void main(String[] args) throws Exception {
    Transportadora tr = new Transportadora();
    tr.EntradaConfig();
    tr.menu();
   }

   public void EntradaConfig()throws Exception{
    this.reader = new BufferedReader(new InputStreamReader(System.in));
    String config;
    System.out.println("Entre com arquivo de configuração(caso nulo arquivo padrão = arqConfig.csv)");
    config = this.reader.readLine();
    config=config.trim();
    if (config.equals("")) {
        config = "arqConfig.csv";
        carregarConfiguracoes(config);
    } else {
        carregarConfiguracoes(config);
    }
   }

   public void carregarConfiguracoes(String config) throws Exception{
    String linha;
    int linhaLeitura=0;
    BufferedReader arquivo = new BufferedReader(new FileReader(config));

    while ((linha = arquivo.readLine()) != null) {
        String confEntregas[] = linha.split(";");
        linhaLeitura++;
        if (linhaLeitura != 1) {
            if(confEntregas[1].equals("EE")){
                this.valorExpresso = Float.parseFloat(confEntregas[2]);
            }
            if (confEntregas[1].equals("EN")) {
                this.valorNormal =  Float.parseFloat(confEntregas[2]);
            }
        }
    }
    arquivo.close();

   }
   
   public void menu()throws Exception{
    String resposta="";
    while (!resposta.equals("4")) {
        System.out.println("+-+-+-+-+-+-Menu-+-+-+-+-+-+");
        System.out.println("[1]-Anexar Arquivo de Leitura");
        System.out.println("[2]-Exibir Encomendas Expressas");
        System.out.println("[3]-Exibir Encomendas Normais");
        System.out.println("[4]-Sair");
        System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+");

        resposta = this.reader.readLine();
        menuCase(resposta);
    }
   }

   public  void menuCase(String resposta)throws Exception{
    switch (resposta) {
        case "1":
            anexarArquvio();
            break;
    
        case "2":
            listarExpressa();
            break;
    
        case "3":
            listarNormal();
            break;
    
        default:
            break;
    }
   }

   public void anexarArquvio()throws Exception{
        System.out.println("Entre com o nome do Arquivo que deseja ler");
        importarDados(this.reader.readLine());
   }

   public void importarDados(String dados)throws Exception{
    String linha;
    int linhaLeitura = 0;
    String data;
    float peso ;
    int codigo ;
    String telefone;
    int prazo; 

    BufferedReader arquivo = new BufferedReader(new FileReader(dados));

     while ((linha = arquivo.readLine()) != null) {
        String entregas[] = linha.split(";");
        linhaLeitura++;
        if (linhaLeitura !=1) {
            if (entregas[2].equals("EE")) {
                codigo = Integer.parseInt(entregas[0]);
                data = entregas[1];
                prazo = Integer.parseInt(entregas[3]);
                peso = Float.parseFloat(entregas[4]);
                telefone = entregas[5];
                registraEE(codigo, fomataData(data), peso, telefone, prazo);
            }

            if (entregas[2].equals("EN")) {
                codigo = Integer.parseInt(entregas[0]);
                data = entregas[1];
                peso = Float.parseFloat(entregas[4]);
                registraEN(codigo, fomataData(data), peso);
            }
        }     
     }
     arquivo.close();
    }

    public void registraEN(int nroPedido, GregorianCalendar data, float peso){
        Encomenda encomenda = new Encomenda();

        encomenda.setNroPedido(nroPedido);
        encomenda.setPeso(peso);
        encomenda.setPostagem(data);
        encomenda.setValorfrete(frete(peso));

        this.vetEncomendas[qtdEncomenda] = encomenda;
        this.qtdEncomenda++;
    }

    public void registraEE(int nroPedido, GregorianCalendar data, float peso, String telefone,int prazo){
        EncomendaExpressa encomendaExpressa = new EncomendaExpressa();
        
        encomendaExpressa.setFone(telefone);
        encomendaExpressa.setNroPedido(nroPedido);
        encomendaExpressa.setPeso(peso);
        encomendaExpressa.setPostagem(data);
        encomendaExpressa.setPrazoEntrega(prazo);
        encomendaExpressa.setValorfrete(freteExpresso(peso,prazo));

        this.vetEncomendasExpressas[qtdEncomendaExpressa] = encomendaExpressa;
        this.qtdEncomendaExpressa++;
    }

    public float frete(float peso){
        return peso * this.valorNormal;
    }
    
    public float freteExpresso(float peso, int prazo){
        if (prazo >= 2) {
            return peso * this.valorExpresso + ((peso * this.valorExpresso )* 0.25f);
        }else{
           return peso * this.valorExpresso; 
        }
    }

    public GregorianCalendar fomataData(String data){
        GregorianCalendar agora =  new GregorianCalendar();
        String[] vetData = data.split("/");
        agora.set(
            Integer.parseInt(vetData[2]),
            Integer.parseInt(vetData[1])-1,     
            Integer.parseInt(vetData[0])
        );   
        return agora;
    }

    public void listarExpressa(){
        DecimalFormat df = new DecimalFormat("0.00");
        int nroPedido;
        float peso;
        //float frete;

        System.out.println("Numero Pedido  "+"  Peso   " + "   Valor Frete   ");
        for (int i = 0; i < qtdEncomendaExpressa; i++) {
            EncomendaExpressa ee = this.vetEncomendasExpressas[i];
            if (ee.getPrazoEntrega() == 1 ) {
                emergencia(ee.getNroPedido(), ee.getPeso(), ee.getValorfrete(),ee.getPrazoEntrega(), qtdAlert);
                this.qtdAlert++;
            }else
            //frete = ee.getValorfrete();
            peso = ee.getPeso();
            nroPedido = ee.getNroPedido();
            System.out.println(nroPedido + "             " + ee.getPeso()+"        R$ "+df.format(ee.getValorfrete()));    
        }
        System.out.println(" ");
        System.out.println("--- ENCOMENDAS COM O PRAZO DE 1 DIA ---");
        System.out.println("Numero Pedido  "+"  Peso   " + "   Valor Frete   ");

        listarAlerts();
        this.qtdAlert = 0;
    }
    public void emergencia(int nroPedido,float peso, float frete,int prazo, int qtdAlert){
        this.nroAlert[qtdAlert] = nroPedido;
        this.pesoAlert[qtdAlert] = peso;
        this.freteAlert[qtdAlert] = freteExpresso(peso, prazo); 
    }

    public void  listarAlerts(){
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < qtdAlert; i++) {
            System.out.println(this.nroAlert[i] +"             "+  this.pesoAlert[i]+"         R$ "+ df.format(this.freteAlert[i]));
        }
    }
    

    public void listarNormal(){
        DecimalFormat df = new DecimalFormat("0.00");
        int nroPedido;
        float peso;
        float frete;
        System.out.println("Numero Pedido  "+"  Peso   " + "   Valor Frete   ");
        for (int i = 0; i < qtdEncomenda; i++) {
            Encomenda ee = this.vetEncomendas[i];
            frete = ee.getValorfrete();
            peso = ee.getPeso();
            nroPedido = ee.getNroPedido();
            System.out.println(nroPedido + "             " +peso+"        R$ "+df.format(frete));    
        }
    }

}