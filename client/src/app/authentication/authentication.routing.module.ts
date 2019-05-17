import {RouterModule, Routes} from '@angular/router';
import {NgModule} from "@angular/core";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {ForbiddenResourceComponent} from "./forbidden-resource/forbidden-resource.component";

const authenticationRoutes: Routes = [
    {path: "login", component: LoginComponent},
    {path: "register", component: RegisterComponent},
    {path: "forbidden", component: ForbiddenResourceComponent},
    {path: '**', redirectTo: "/"}
];

@NgModule({
    exports: [
        RouterModule
    ],
    imports: [
        RouterModule.forChild(authenticationRoutes)
    ],
})
export class AuthenticationRoutingModule {

}
