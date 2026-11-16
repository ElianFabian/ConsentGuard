// Portuguese (Portugal and Brazil)

val code = "pt"
val name = "Portuguese"
val status = "reviewed"
val conj = listOf("e")

val pack = mapOf(
    "reject" to listOf(
        "rejeitar tudo", "rejeitar todos", "rejeitar todas", "rejeitar todos os cookies",
        "rejeitar cookies", "recusar tudo", "recusar todos", "recusar todas", "recusar cookies",
        "recusar cookies opcionais", "continuar sem aceitar", "prosseguir sem aceitar",
        "continuar sem consentir", "não aceitar", "não aceito", "não concordo", "não obrigado",
        "não obrigada", "desativar tudo", "desativar todos", "bloquear tudo", "rejeitar e fechar",
        "rejeitar e continuar", "=rejeitar", "=recusar", "=negar", "=não", "=discordo", "=opor se",
        "não consentir", "desmarcar tudo", "sem aceitar"
    ),
    "necessary" to listOf(
        "apenas necessários", "apenas os necessários", "apenas cookies necessários",
        "somente necessários", "somente os necessários", "somente cookies necessários",
        "só os necessários", "apenas essenciais", "apenas os essenciais", "apenas cookies essenciais",
        "somente essenciais", "aceitar apenas necessários", "aceitar apenas os necessários",
        "aceitar somente os necessários", "permitir apenas os necessários",
        "continuar com os necessários", "guardar apenas os necessários", "apenas o necessário",
        "só o necessário", "=cookies necessários", "=cookies essenciais", "=necessários", "=essenciais",
        "=apenas necessários", "=somente necessários", "=apenas essenciais", "apenas o essencial",
        "somente o essencial"
    ),
    "manage" to listOf(
        "gerir preferências", "gerenciar preferências", "gerir opções", "gerenciar opções",
        "gerir cookies", "gerenciar cookies", "gerir consentimento", "gerenciar consentimento",
        "definições de cookies", "configurações de cookies", "configurar cookies",
        "personalizar cookies", "personalizar opções", "definições de privacidade",
        "configurações de privacidade", "preferências de privacidade", "mais opções", "outras opções",
        "opções adicionais", "mostrar detalhes", "ver detalhes", "mostrar finalidades",
        "mostrar parceiros", "lista de parceiros", "lista de fornecedores", "finalidades e parceiros",
        "escolher cookies", "centro de preferências", "centro de privacidade", "quero escolher",
        "=definições", "=configurações", "=preferências", "=opções", "=personalizar", "=configurar",
        "=detalhes", "=gerir", "=gerenciar", "=mais opções"
    ),
    "confirm" to listOf(
        "guardar preferências", "salvar preferências", "guardar definições", "salvar configurações",
        "guardar escolhas", "salvar escolhas", "guardar seleção", "salvar seleção", "guardar e fechar",
        "salvar e fechar", "guardar e continuar", "salvar e continuar", "confirmar escolhas",
        "confirmar seleção", "confirmar preferências", "confirmar as minhas escolhas",
        "confirmar minhas escolhas", "aplicar definições", "aplicar configurações", "aceitar seleção",
        "permitir seleção", "guardar alterações", "salvar alterações", "=guardar", "=salvar",
        "=confirmar", "=aplicar", "=concluído", "=feito"
    ),
    "fallbackReject" to listOf(
        "rejeitar", "recusar", "=não", "=não obrigado", "=não obrigada", "=não aceito"
    ),
    "rejectButton" to listOf("=rejeitar", "=recusar", "=rejeitar tudo", "=não", "=desativar"),
    "expandSection" to listOf(
        "finalidades especiais", "funcionalidades especiais", "funções especiais"
    ),
    "anchor" to listOf(
        "utilizamos cookies", "usamos cookies", "este site utiliza cookies", "este site usa cookies",
        "nós e os nossos parceiros", "nós e nossos parceiros", "a sua privacidade", "sua privacidade",
        "interesse legítimo", "interesses legítimos", "lista de parceiros", "definições de privacidade",
        "o seu consentimento", "seu consentimento", "nossos parceiros", "nossos fornecedores",
        "os nossos parceiros", "anúncios personalizados", "armazenar e ou aceder a informações",
        "finalidades do tratamento", "política de cookies", "aviso de cookies",
        "tecnologias de rastreamento", "ao continuar a navegar", "ao continuar navegando"
    ),
    "bareAnchor" to listOf("cookies"),
    "strongAnchor" to listOf(
        "estritamente necessários", "cookies necessários", "cookies essenciais", "cookies funcionais",
        "cookies de desempenho", "cookies estatísticos", "cookies analíticos", "cookies de marketing",
        "cookies publicitários", "anúncios personalizados", "interesse legítimo", "lista de parceiros",
        "nossos parceiros", "os nossos parceiros", "sempre ativo", "sempre ativos",
        "finalidades do tratamento", "preferências de cookies", "definições de cookies",
        "configurações de cookies", "categorias de cookies", "finalidades especiais",
        "tecnologias de rastreamento"
    ),
    "neverClick" to listOf(
        "subscrever", "assinar", "assinatura", "comprar", "pagar", "pagamento", "iniciar sessão",
        "entrar", "login", "registar", "registrar", "criar conta", "tornar se membro", "teste gratuito",
        "experimentar grátis", "doar", "donativo", "apoiar", "sem anúncios", "sem publicidade",
        "remover anúncios", "por mês", "por ano", "mensal", "anual", "premium", "encomendar"
    ),
    "acceptGuard" to listOf(
        "aceitar tudo", "aceitar todos", "aceitar todas", "aceitar todos os cookies", "aceitar cookies",
        "permitir tudo", "permitir todos", "permitir todos os cookies", "permitir cookies",
        "aceitar e fechar", "aceitar e continuar", "aceitar e navegar", "aceito tudo", "=aceito",
        "=concordo", "concordo e continuar", "dou o meu consentimento", "sim aceito", "sim concordo",
        "ok entendi", "=aceitar", "=permitir", "=consentir", "=de acordo", "=ok", "=okay", "=sim",
        "=entendi", "=continuar", "=ativar", "=tudo"
    ),
    "acceptWords" to listOf("aceit*", "permit*", "consent*", "autoriz*", "concord*", "acordo"),
    "switchEssential" to listOf(
        "estritamente necessário", "estritamente necessários", "necessário", "necessários",
        "necessária", "essencial", "essenciais", "obrigatório", "obrigatórios", "sempre ativo",
        "sempre ativos", "sempre ativado", "requerido", "tecnicamente necessário",
        "não pode ser desativado", "não podem ser desativados", "indispensável",
        "para o funcionamento do site", "funcionamento do site"
    ),
    "switchNonEssential" to listOf(
        "não essencial", "não essenciais", "não necessário", "não necessários", "não necessária",
        "dispensável", "opcional", "opcionais", "facultativo", "facultativos", "adicional", "adicionais"
    ),
    "switchKeepOn" to listOf(
        "não vender", "não partilhar", "não compartilhar", "não rastrear", "lembrar a minha escolha",
        "lembrar minha escolha", "manter sessão iniciada", "não mostrar novamente",
        "não perguntar novamente"
    ),
    "adsPurpose" to listOf(
        "publicidade", "publicitár", "anúncios", "marketing", "segmentação", "personaliz",
        "remarketing", "baseada em interesses", "perfil", "redes sociais", "seleção de anúncios"
    ),
    "statsPurpose" to listOf(
        "estatística", "estatísticas", "estatístic", "análise", "analítica", "medição", "audiência",
        "desempenho", "dados de utilização", "dados de uso", "visitantes", "tráfego", "melhorar",
        "pesquisa"
    ),
    "topLevelWords" to listOf(
        "tudo", "todos", "todas", "apenas", "somente", "só", "necessários", "necessário", "essenciais",
        "essencial", "cookies", "opcionais", "rastreamento", "técnicos"
    ),
    "cookieContext" to listOf(
        "utilizamos", "usamos", "utiliza cookies", "usa cookies", "aceitar", "consent", "consentimento",
        "privacidade", "política", "definições", "configurações", "preferências", "parceiros",
        "personaliz", "rastreamento", "publicidade", "dados"
    ),
    "sensitiveScreen" to listOf(
        "número do cartão", "numero do cartao", "cartão de crédito", "cartão de débito",
        "titular do cartão", "data de validade", "código de segurança", "cvv", "cvc", "iban",
        "número da conta", "dados de pagamento", "método de pagamento", "pagar agora"
    )
)
