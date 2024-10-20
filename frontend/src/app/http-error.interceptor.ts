import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((err : HttpErrorResponse) => {
      alert("HTTP Error: " + err.error.status + " - " + err.error.message);
      return throwError(() => err);
    })
  );
};
