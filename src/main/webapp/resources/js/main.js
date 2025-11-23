(function() {
    const username = window.appConfig.username;
    const context = window.appConfig.contextPath;

    if (!username) return;

    const wsUrl = (window.location.protocol === "https:" ? "wss" : "ws")
        + "://" + window.location.host
        + context + "/chat/" + encodeURIComponent(username);

    const socket = new WebSocket(wsUrl);

    const messagesEl = document.getElementById("chatMessages");

    socket.addEventListener("message", evt => {
        try {
            const msg = JSON.parse(evt.data);
            const div = document.createElement("div");
            div.textContent = (msg.from ? msg.from + ": " : "") + msg.content;
            messagesEl.appendChild(div);
            messagesEl.scrollTop = messagesEl.scrollHeight;
        } catch(e) {
            console.error("Invalid message", e);
        }
    });

    window.chatSocket = socket;
})();
