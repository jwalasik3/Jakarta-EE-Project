(function() {
    if (!window.appConfig || !window.appConfig.username || !window.appConfig.contextPath) {
        return;
    }

    const username = window.appConfig.username;
    if (!username) return;

    const scheme = window.location.protocol === "https:" ? "wss" : "ws";
    const wsUrl = scheme + "://" + window.location.host
        + window.appConfig.contextPath
        + "/chat/" + encodeURIComponent(username);

    let socket;
    try {
        socket = new WebSocket(wsUrl);
    } catch (e) {
        return;
    }

    let messagesEl = document.getElementById("chatMessages");
    function ensureMessagesEl() {
        if (!messagesEl) {
            const div = document.createElement('div');
            div.id = 'chatMessages';
            document.body.appendChild(div);
            messagesEl = div;
        }
    }

    function appendMessage(text) {
        ensureMessagesEl();
        const div = document.createElement("div");
        div.textContent = text;
        messagesEl.appendChild(div);
        messagesEl.scrollTop = messagesEl.scrollHeight;
    }

    socket.addEventListener("open", function() {
        appendMessage("[SYSTEM] Connected as " + username);
    });

    socket.addEventListener("message", function(evt) {
        try {
            const data = JSON.parse(evt.data);
            if (data.from && data.content) {
                appendMessage(data.from + ": " + data.content);
            } else {
                appendMessage(evt.data);
            }
        } catch (e) {
            appendMessage(evt.data);
        }
    });

    socket.addEventListener("close", function() {
        appendMessage("[SYSTEM] Disconnected.");
    });

    socket.addEventListener("error", function() {
        appendMessage("[SYSTEM] WebSocket error occurred.");
    });

    window.chatSocket = socket;
    window.appendChat = appendMessage;
})();
