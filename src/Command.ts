export abstract class Command {

    abstract name: string;
    abstract description: string;

    abstract run(interaction, complete);

}