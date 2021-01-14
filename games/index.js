const Hangman =  require('./hangman')

module.exports = class Games{

    constructor(client){
        this.client = client
        this.hangman = new Hangman(client)
    }

    startHangman(message, params){
        this.hangman.start(message)
    }
}
