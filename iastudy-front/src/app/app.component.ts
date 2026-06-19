import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StudyService, StudyResponse, Flashcard } from './study.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private studyService = inject(StudyService);

  userInput = '';
  loading = signal(false);
  result = signal<StudyResponse | null>(null);
  quantity = 5;

send() {
  if (!this.userInput.trim()) return;

  this.loading.set(true);
  this.result.set(null);

  this.studyService.analyze(this.userInput, this.quantity).subscribe({
    next: (res) => {
      console.log('Backend Response:', res);

      if (res && res.flashcards) {
        const formattedCards = res.flashcards.map(card => ({
          ...card,
          isOpen: false
        }));

        this.result.set({
          summary: res.summary,
          flashcards: formattedCards
        });
      }

      this.loading.set(false);
    },
    error: (err) => {
      console.error('Error:', err);
      alert('Server connection error. Please ensure the backend is running on port 8080.');
      this.loading.set(false);
    }
    });
}

  formatText(text: string): string {
    if (!text) return '';
    return text.replace(/\*\*(.*?)\*\*/g, '<b>$1</b>');
  }

  toggleCard(index: number) {
    const res = this.result();
    if (res && res.flashcards[index]) {
      res.flashcards[index].isOpen = !res.flashcards[index].isOpen;
      this.result.set({ ...res });
    }
  }
}
