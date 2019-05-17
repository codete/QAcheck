import {NgModule} from "@angular/core";
import {MarkdownModule} from 'ngx-markdown';
import {DocumentationComponent} from "./documentation.component";
import {MatTabsModule} from '@angular/material/tabs';

@NgModule({
    declarations: [
        DocumentationComponent
    ],
    imports: [
        MatTabsModule,
        MarkdownModule.forRoot()
    ],
    exports: [
        DocumentationComponent
    ]
})
export class DocumentationModule {
}
