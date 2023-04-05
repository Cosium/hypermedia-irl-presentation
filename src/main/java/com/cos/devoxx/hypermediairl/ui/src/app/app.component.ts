import { Component, OnInit } from '@angular/core';
import { KettingFactory } from './ketting.factory';
import { Ketting } from 'ketting';

export type Item = { label: string; price: number };

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  private readonly ketting: Ketting;
  loaded = false;

  constructor(kettingFactory: KettingFactory) {
    this.ketting = kettingFactory.build();
  }

  ngOnInit(): void {
    this.init();
  }

  async init(): Promise<void> {
    const itemsResource = await this.ketting.follow('items');
    const items = await itemsResource.get({headers: {'Prefer': 'return=minimal'}});
    const createAction = items.action('create');
    await Promise.all([createAction.submit({label: 'Ray-Ban Devoxx', price: '98.25'}),
      createAction.submit({label: 'Verre Devoxx', price: '123.25'}),
      createAction.submit({label: 'Verre Devoxx', price: '123.25'})
    ]);
    this.loaded = true;
  }
}
