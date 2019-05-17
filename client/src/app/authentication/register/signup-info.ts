export class SignUpInfo {
    public firstName: string;
    public lastName: string;
    public username: string;
    public email: string;
    public password: string;

    constructor(firstName: string, lastName: string, username: string, email: string, password: string) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
