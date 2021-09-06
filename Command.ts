export abstract class Command {

    name: String;

    abstract run(interaction, complete);

}