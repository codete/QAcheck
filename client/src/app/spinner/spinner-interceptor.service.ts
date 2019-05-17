import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpInterceptor,
  HttpResponse
} from '@angular/common/http';
import { tap, catchError } from 'rxjs/operators';
import { SpinnerService } from './spinner.service';

@Injectable()
export class SpinnerInterceptor implements HttpInterceptor {
  private totalRequests = 0;

  constructor(private spinnerService: SpinnerService) { }

  public intercept(request: HttpRequest<any>, next: HttpHandler) {
    this.totalRequests++;
    this.spinnerService.loading = true;
    return next.handle(request).pipe(
      tap((res) => {
        if (res instanceof HttpResponse) {
          this.decreaseRequests();
        }
      }),
      catchError((err) => {
        this.decreaseRequests();
        throw err;
      })
    );
  }

  private decreaseRequests() {
    this.totalRequests--;
    if (this.totalRequests === 0) {
      this.spinnerService.loading = false;
    }
  }
}
