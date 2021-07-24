import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import * as AWS from 'aws-sdk/global';
import * as S3 from 'aws-sdk/clients/s3';
import { Receipt } from '../model/Receipt';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  // API url
  FOLDER = "receipts/"
    
  constructor(private http:HttpClient) { }
  
  upload(files: Array<File>) {

    let res: Array<Receipt> = [];
    
    const bucket = new S3(
        {
            accessKeyId: 'AKIA6NRDH5WGL4ZYFWI5',
            secretAccessKey: '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',
            region: 'us-east-2'
        }
    );

    for(var i = 0; i<files.length; i++) {
      const contentType = files[i].type;
      const params = {
        Bucket: 'cop-mobile',
        Key: this.FOLDER + files[i].name,
        Body: files[i],
        ACL: 'public-read',
        ContentType: contentType
    };

    bucket.upload(params, function (err, data) {
      if (err) {
          console.log('There was an error uploading your file: ', err);
          return false;
      }
      console.log('Successfully uploaded file.', data);
      return true;
    });
  }

    console.log(res)

    return res;
  }
}
