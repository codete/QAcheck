import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'documentation',
    templateUrl: 'documentation.component.html'
})
export class DocumentationComponent implements OnInit, OnDestroy {

    public document: string;
    private routeParamsSubscription: Subscription;

    constructor(private route: ActivatedRoute) {

    }

    public ngOnInit() {
        this.routeParamsSubscription = this.route.params.subscribe((params) => {
            if (params.id) {
                this.document = './assets/documentation/' + params.id + '.md';
            } else {
                this.document = './assets/documentation/start.md';
            }
        });
    }

    public ngOnDestroy() {
        if (this.routeParamsSubscription) {
            this.routeParamsSubscription.unsubscribe();
        }
    }

}
