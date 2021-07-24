import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateToString',
  pure: true
})

export class DateToStringPipe implements PipeTransform {
    transform(date: Date, args?: any): any {
        // var month = format(todayTime .getMonth() + 1);
        // var day = format(todayTime .getDate());
        // var year = format(todayTime .getFullYear());
        // return month + "/" + day + "/" + year;
        return date.toISOString().slice(0, 10);;
    }
}