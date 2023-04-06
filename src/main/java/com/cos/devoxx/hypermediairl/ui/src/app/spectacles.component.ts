import { Component, OnInit } from '@angular/core';
import { Ketting, State } from 'ketting';
import { Item } from './app.component';
import { Resources } from './resources';
import { FormsModule } from '@angular/forms';
import { KettingFactory } from './ketting.factory';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { NgForOf } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

type Spectacles = {
  frameLabel: string;
  framePrice: number;
  rightLensLabel: string;
  rightLensPrice: number;
  leftLensLabel: string;
  leftLensPrice: number;
}

@Component({
  selector: 'spectacles',
  templateUrl: './spectacles.component.html',
  styleUrls: ['./spectacles.component.css'],
  imports: [
    MatInputModule,
    MatSelectModule,
    NgForOf,
    MatCardModule,
    MatButtonModule,
    FormsModule
  ],
  standalone: true
})
export class SpectaclesComponent implements OnInit {


  private readonly ketting: Ketting;
  items: State<Item>[] = [];
  spectacles?: State<Spectacles>;
  private _selectedFrame?: State<Item>;
  private _selectedRightLens?: State<Item>;
  private _selectedLeftLens?: State<Item>;

  constructor(kettingFactory: KettingFactory) {
    this.ketting = kettingFactory.build();
  }

  ngOnInit(): void {
    this.loadItems();
    //this.createSpectacles();
    this.create();
  }

  async create(): Promise<void> {
    const spectaclesResource = await this.ketting.follow<Spectacles>('spectacles');
    const savedResource = await spectaclesResource.postFollow({});
    this.spectacles = await savedResource.get();
    console.log(this.spectacles);
  }

  /*async createSpectacles(): Promise<void> {
    const spectaclesResource = await this.ketting.follow<Spectacles>('spectacles');
    const spectaclesState = await spectaclesResource.get({headers: {'Prefer': 'return=minimal'}});
    console.log(spectaclesState);
    spectaclesState.action('create').submit({});
  }*/

  private async loadItems(): Promise<void> {
    const itemResources = await this.ketting.follow<Item>('items');
    const itemsState = await itemResources.get();
    this.items = await new Resources(itemsState.followAll('content')).toStates();
  }

  async updateFrame(): Promise<void> {
    if (!this.spectacles || !this._selectedFrame) {
      return;
    }
    console.log(this.spectacles);
    await this.spectacles.action('updateFrame').submit({itemUri: this._selectedFrame.uri});
    this.loadSpectacles();
  }

  async loadSpectacles(): Promise<void> {
    this.spectacles = await this.ketting.go(this.spectacles?.uri).get();
    console.log(this.spectacles);
  }

  async updateRightLens(): Promise<void> {
    if (!this.spectacles) {
      return;
    }
    await this.spectacles.action('updateRightLens').submit({itemUri: this._selectedRightLens?.uri});
    this.loadSpectacles();
  }

  async updateLeftLens(): Promise<void> {
    if (!this.spectacles) {
      return;
    }
    await this.spectacles.action('updateLeftLens').submit({itemUri: this._selectedLeftLens?.uri});
    this.loadSpectacles();
  }

  canInvoice() {
    if (!this.spectacles) {
      return false;
    }
    return this.spectacles.hasAction('invoice');
  }

  async invoice(): Promise<void> {
    if (!this.spectacles) {
      return;
    }
    this.spectacles?.action('invoice').submit({});
  }

  set selectedFrame(value: State<Item>) {
    this._selectedFrame = value;
    this.updateFrame();
  }

  set selectedRightLens(value: State<Item>) {
    this._selectedRightLens = value;
    this.updateRightLens();
  }

  set selectedLeftLens(value: State<Item>) {
    this._selectedLeftLens = value;
    this.updateLeftLens();
  }
}
