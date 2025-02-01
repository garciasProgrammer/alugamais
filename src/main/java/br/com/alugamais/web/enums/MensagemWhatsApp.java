package br.com.alugamais.web.enums;

public enum MensagemWhatsApp {


    MENSAGEM_PAGAMENTO_WHATSAPP("%s\n" +
            "Contrato N°: %s\n" +
            "Imóvel N°: %s\n" +
            "Nome: %s\n" +
            "*********************\n" +
            "*Informe de Pagamento*\n" +
            "**********************\n" +
            "Data Pagamento: %s\n" +
            "Ref: %s\n" +
            "Valor R$: %s");

    private String template;

    MensagemWhatsApp(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    // Método para substituir os placeholders pelos valores reais
    public String format(Object... args) {
        return String.format(template, args);
    }

}
