import { HTTPError, KyRequest, KyResponse, NormalizedOptions } from 'ky';

export class ApiError<T = unknown> extends HTTPError<T> {
  public status: number;
  public message: string;

  constructor(request: KyRequest, options: NormalizedOptions, response: KyResponse<T>, message: string) {
    super(response, request, options);
    this.status = response.status;
    this.message = message;
  }
}
