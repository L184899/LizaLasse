async function loadUsers() {
    const container = document.getElementById("user-list");

    const backendURL = "https://lizalasse.onrender.com/data";

    try {
        const response = await fetch(backendURL);
        const users = await response.json();

        container.innerHTML = ""; // fjern "laster..."

        if (users.length === 0) {
            container.innerHTML = "<p class='loading'>Ingen brukere funnet.</p>";
            return;
        }

        users.forEach(u => {
            const card = document.createElement("div");
            card.className = "user-card";

            card.innerHTML = `
                <h3>${u.name}</h3>
                <p><strong>Email:</strong> ${u.email}</p>
                <p><strong>ID:</strong> ${u.id}</p>
            `;

            container.appendChild(card);
        });

    } catch (err) {
        container.innerHTML = "<p class='loading'>Feil: backend svarte ikke.</p>";
        console.error(err);
    }
}

loadUsers();
