var app = new Vue({
    el:"#app",
    data:{
        clientAccounts: [],
        clientAccountsTo: [],
        debitCards: [],
        errorToats: null,
        errorMsg: null,
        accountFromNumber: "VIN",
        accountToNumber: "VIN",
        trasnferType: "own",
        amount: 0,
        description: "",
        randomKeys: [],
        randomKeysValues:{
            k1:null,
            k2:null,
            k3:null,
        }
    },
    methods:{
        getData: function() {
            axios.get("/api/clients/current/accounts")
                .then((response) => {
                    this.clientAccounts = response.data;
                })
                .catch((error) => {
                    this.errorMsg = "Error getting client accounts data";
                    this.errorToats.show();
                });

            axios.get("/api/transactions/coordinate-card")
                .then((response) => {
                    this.randomKeys = response.data;
                })
                .catch((error) => {
                    this.errorMsg = "Error getting coordinate card data";
                    this.errorToats.show();
                });
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        checkTransfer: function(){
            if(this.accountFromNumber == "VIN"){
                this.errorMsg = "You must select an origin account";  
                this.errorToats.show();
            }
            else if(this.accountToNumber == "VIN"){
                this.errorMsg = "You must select a destination account";  
                this.errorToats.show();
            }else if(this.amount == 0){
                this.errorMsg = "You must indicate an amount";  
                this.errorToats.show();
            }
            else if(this.description.length <= 0){
                this.errorMsg = "You must indicate a description";  
                this.errorToats.show();
            }else{
                this.modal.show();
            }
        },
        transfer: function(){
            //this.valueKeys.push(document.getElementById())
            axios.post("/api/transactions",
            {
            amount: this.amount,
            description: this.description,
            fromAccountNumber: this.accountFromNumber,
            toAccountNumber: this.accountToNumber,
            randomKeys: this.randomKeys,
            randomKeysValues : [this.randomKeysValues.k1, this.randomKeysValues.k2,this.randomKeysValues.k3]
            
            })
            .then(response => { 
                this.modal.hide();
                this.okmodal.show();
            })
            .catch((error) =>{
                this.errorMsg = error.response.data;  
                this.errorToats.show();
            })
        },
        changedType: function(){
            this.accountFromNumber = "VIN";
            this.accountToNumber = "VIN";
        },
        changedFrom: function(){
            if(this.trasnferType == "own"){
                this.clientAccountsTo = this.clientAccounts.filter(account => account.number != this.accountFromNumber);
                this.accountToNumber = "VIN";
            }
        },
        finish: function(){
            window.location.reload();
        },
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"   
                this.errorToats.show();
            })
        },
    },
    mounted: function() {
      this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
      this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
      this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
      this.getData();
      }
})
