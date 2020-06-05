import {Component, OnInit, ViewChild} from '@angular/core';
import {AccountService} from '../../services/api/account/account.service';
import {UserCreate} from '../../services/api/account/AccountDto';
import {Router} from '@angular/router';
import {ToastService} from '../../services/toast/toast.service';
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.sass']
})
export class RegisterUserComponent implements OnInit {

  userCreate: UserCreate = new UserCreate();

  @ViewChild(NgForm, { static: false }) form;

  constructor(private router: Router,
              private toastService: ToastService,
              private accountService: AccountService) {
  }

  ngOnInit() {
  }

  createUser() {
    if (this.form.invalid) {
      return;
    }

    this.accountService.createUser(this.userCreate).subscribe(response => {
      this.router.navigateByUrl('');
      this.toastService.showToast('Successfully created account!');
    });
  }
}
