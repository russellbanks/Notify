export abstract class Command {

    abstract name: string;
    abstract description: string;

    discord;

    constructor(discord: any) {
        this.discord = discord;
    }

    abstract run(interaction, complete);

}