// Criar um WebSocket global
const WebSocketManager = (function () {
    let stompClient = null;
    let isConnected = false;
    let tenantId = window.location.hostname.split('.')[0]; // Obtém o tenant da URL

    function connect() {
        if (isConnected) {
            console.log("WebSocket já está conectado!");
            return;
        }

        const socket = new SockJS("/ws"); // Conecta ao servidor WebSocket
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function () {
            console.log(`✅ Conectado ao WebSocket do tenant ${tenantId}`);
            isConnected = true;

            // Inscreve-se no tópico do tenant para receber notificações
            stompClient.subscribe(`/topic/${tenantId}/cards`, function (message) {
                const data = JSON.parse(message.body);
                console.log("🔔 Mensagem recebida via WebSocket:", data);

                // Exibir notificação na tela
                exibirNotificacao(data);
                updateCardParcels(data.contratoId)
                iniciarContadorPix(); // Agora o contador será reiniciado corretamente
            });
        }, function (error) {
            console.error("❌ Erro na conexão WebSocket:", error);
        });
    }

    return {
        connect: connect
    };
})();

// Conectar automaticamente ao carregar a página
document.addEventListener("DOMContentLoaded", function () {
    WebSocketManager.connect();
});

// Função para exibir notificação na tela
function exibirNotificacao(data) {
    const notificacao = document.getElementById("notificacao");

    // Obtém a mensagem correta (ajusta caso o backend mude o formato no futuro)
    const mensagem = data.mensagem || "Atualização recebida!";


    notificacao.textContent = mensagem;
    notificacao.classList.remove("d-none", "alert-success", "alert-danger");

    // Define cor com base no conteúdo da mensagem
    if (data.action.toLowerCase().includes("sucesso") || data.action.toLowerCase().includes("recebido")) {
        notificacao.classList.add("alert", "alert-success"); // Verde 🟢
    } else {
        notificacao.classList.add("alert", "alert-danger"); // Vermelho 🔴
    }

    // Exibe o alerta e oculta após 5 segundos
    setTimeout(() => {
        notificacao.classList.add("d-none");
    }, 10000);
}

function updateCardParcels(contratoId) {
    <!-- carrega dados de parcelas ao clicar em uma linha da tabela de contratos-->
    const parcelasContratoCard = document.getElementById('parcelasContratoCard');

    if(document.getElementById('pixResult')){
        document.getElementById('pixResult').style.display = 'none';
        document.getElementById('qrCodeImage').value = '';;
        document.getElementById('copiaCola').value = '';

        limpaCampos();
        if (contratoId) {
            // Faz uma requisição AJAX para buscar os dados
            fetch(`/pagamento/extrato-financeiro/${contratoId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erro ao carregar os dados do contrato.');
                    }
                    return response.json();
                })
                .then(data => {
                    // Exibe o card e preenche os dados
                    parcelasContratoCard.classList.remove('d-none');
                    preencherCard(data);
                })
                .catch(error => {
                    console.error('Erro ao carregar os dados:', error);
                });
        }
    }

}

/**
 * Preenche o card com os dados retornados de parcelas e contrato
 * @param {Object} data - Dados retornados do servidor
 */
function preencherCard(data) {

    const tituloContrato = document.getElementById('tituloContrato');
    if (tituloContrato) {
        tituloContrato.innerText = `Registrar Pagamento - Contrato N° ${data.contrato.id}`;
    }
    document.getElementById('locatarioNome').value = data.contrato.locatario.nome || '';
    document.getElementById('locadorNome').value = data.contrato.locador.nome || '';
    document.getElementById('locatarioCelular').value = data.contrato.locatario.celularWhatsApp || '';
    document.getElementById('imovelNumero').value = data.contrato.imovel.numero || '';
    document.getElementById('imovelTipo').value = data.contrato.imovel.tipo || '';

    const tabelaParcelas = document.getElementById('tabelaParcelas');
    const tbody = tabelaParcelas.querySelector('tbody');
    tbody.innerHTML = ''; // Limpa as linhas anteriores
    data.parcelas.sort((a, b) => a.parcela - b.parcela);
    // Identifica o último pagamento recebido
    const ultimaRecebida = data.parcelas
        .filter(parcela => parcela.situacao === 'RECEBIDA')
        .pop(); // Última parcela recebida

    data.parcelas.forEach(item => {
        const row = document.createElement('tr');
        const isRecebida = item.situacao === 'RECEBIDA';
        const situacaoParcela = item.situacao === 'RECEBIDA'
            ? 'text-success' // Verde
            : item.situacao === 'EM ATRASO'
                ? 'text-danger'  // Vermelho
                : 'text-primary'; // Azul para ABERTA e outras situações
        row.innerHTML = `
                <td>${item.parcela}</td>
                <td>${item.dataVencimento.split('T')[0].split('-').reverse().join('/')}</td>
                <td>${new Intl.NumberFormat('pt-BR', {style: 'currency', currency: 'BRL'}).format(item.valorAluguel)}</td>
                <td class="${situacaoParcela}">${item.situacao}</td>
                <td>${item.dataPagamento ? item.dataPagamento.split('T')[0].split('-').reverse().join('/') : '-'}</td>
                <td>${item.codigoPagamento ? item.codigoPagamento : '-'}</td>
                <td>
                    <button
                        class="btn btn-success btn-sm"
                        onclick="pagarParcela(${data.contrato.id}, ${item.parcela})"
                        ${isRecebida ? 'disabled' : ''}>
                        <i class="bi bi-currency-dollar"></i> Pagar
                    </button>
                    <button
                        class="btn btn-primary btn-sm"
                        onclick="gerarRecibo(${item.codigoPagamento})"
                        ${isRecebida ? '' : 'disabled'}>
                        <i class="bi bi-receipt"></i> Recibo
                    </button>
                     <button
                    class="btn btn-warning btn-sm"
                    onclick="estornarPagamento(${item.codigoPagamento})"
                    ${item === ultimaRecebida ? '' : 'disabled'}>
                    <i class="bi bi-arrow-counterclockwise"></i> Estorno
                </button>
                </td>
            `;
        tbody.appendChild(row);
    });
}


