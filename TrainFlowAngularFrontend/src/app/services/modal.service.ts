import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  private modalOpen = new BehaviorSubject<boolean>(false);
  modalOpen$ = this.modalOpen.asObservable();

  setOpen(isOpen: boolean) {
    this.modalOpen.next(isOpen);
  }
}
