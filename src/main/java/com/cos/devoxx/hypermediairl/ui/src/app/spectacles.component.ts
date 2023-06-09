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
    // this.createSpectacles();
    this.create();
  }

  async create(): Promise<void> {
    const spectaclesResource = await this.ketting.follow<Spectacles>('spectacles');
    const savedResource = await spectaclesResource.postFollow({});
    this.spectacles = await savedResource.get();
  }

  async createSpectacles(): Promise<void> {
    const spectaclesResource = await this.ketting.follow<Spectacles>('spectacles');
    const spectacles = await spectaclesResource.get();
    spectacles.action('create').submit({});
  }

  private async loadItems(): Promise<void> {
    const itemResources = await this.ketting.follow<Item>('items');
    const itemsState = await itemResources.get();
    this.items = await new Resources(itemsState.followAll('content')).toStates();
  }

  // spectacles: State<Spectacles>;
  async selectFrame(): Promise<void> {
    if (!this.spectacles || !this._selectedFrame) {
      return;
    }
    await this.spectacles.action('selectFrame').submit({itemUri: this._selectedFrame.uri});
    this.loadSpectacles();
  }

  async loadSpectacles(): Promise<void> {
    if (!this.spectacles) {
      return;
    }
    this.spectacles = await this.ketting.go(this.spectacles.uri).get();
  }

  async selectRightLens(): Promise<void> {
    if (!this.spectacles) {
      return;
    }
    await this.spectacles.action('selectRightLens').submit({itemUri: this._selectedRightLens?.uri});
    this.loadSpectacles();
  }

  async selectLeftLens(): Promise<void> {
    if (!this.spectacles) {
      return;
    }
    await this.spectacles.action('selectLeftLens').submit({itemUri: this._selectedLeftLens?.uri});
    this.loadSpectacles();
  }

  canInvoice() {
    if (!this.spectacles) {
      return false;
    }
    return this.spectacles.hasAction('doInvoice');
  }

  async invoice(): Promise<void> {
    if (!this.spectacles) {
      return;
    }
    this.spectacles.action('doInvoice').submit({});
  }

  set selectedFrame(value: State<Item>) {
    this._selectedFrame = value;
    this.selectFrame();
  }

  set selectedRightLens(value: State<Item>) {
    this._selectedRightLens = value;
    this.selectRightLens();
  }

  set selectedLeftLens(value: State<Item>) {
    this._selectedLeftLens = value;
    this.selectLeftLens();
  }
}
