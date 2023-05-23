var app = new Vue({
    el: "#app",
    data: {
        clientInfo: {},
        errorToats: null,
        errorMsg: null,
        successMsg: null,
        loadingModal: null
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        create: function () {
            axios.post('/api/clients/current/accounts')
                .then(response => window.location.reload())
                .catch((error) => {
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        },
        send: function () {
            this.loadingModal = new bootstrap.Modal(document.getElementById('loadingModal'));
            this.loadingModal.show();
            axios.get('/api/clients/current/sendinfo')
                .then(response => {
                    this.loadingModal.hide();
                    this.successMsg = response.data;
                    this.sucessToats.show();
                })
                .catch((error) => {
                    this.loadingModal.hide();
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.sucessToats = new bootstrap.Toast(document.getElementById('success-toast'));
        this.getData();
    }
})