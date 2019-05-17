import {NgModule} from '@angular/core';
import {MenuComponent} from "./menu.component";
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from '@angular/router';

@NgModule({
    declarations: [
        MenuComponent
    ],
    exports: [
        MenuComponent
    ],
    imports: [
        BrowserModule,
        RouterModule
    ],
})

export class MenuModule {

}
