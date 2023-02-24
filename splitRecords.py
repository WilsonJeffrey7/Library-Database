import json
import os
from csv import reader

f = open('books.csv', 'r', encoding = "utf-8")
auth = open('authors.csv', 'w', encoding="utf-8")
book = open('bookinfo.csv', 'w', encoding="utf-8")
book_author_file = open('book_author.csv', 'w', encoding='utf-8')


author_id = 1
authors = set() # all the unique author names
author_ids = dict() # author name -> author id
isbn_authors = dict() # book isbn -> list of author names

book_output = ['isbn\ttitle\n']
next(f) # skipping the header line
for line in f:
    cols = line.split("\t")
    isbn = cols[1]
    title = cols[2]
    book_output_line = f'{isbn}\t{title}\n'
    book_output.append(book_output_line)
    isbn_authors[isbn] = []
    author_names = cols[3].split(',')
    for author_name in author_names:
        isbn_authors[isbn].append(author_name)
        if len(author_name) > 0:
            authors.add(author_name)
    
author_output = ['author_id\tauthor_name\n']
for author_name in sorted(list(authors)):
    author_output_line = f'{author_id}\t{author_name}\n'
    author_output.append(author_output_line)
    author_ids[author_name] = author_id
    author_id += 1

book_author_output = ['author_id\tisbn\n']
for isbn, authors in isbn_authors.items():
    for author_name in authors:
        if len(author_name) > 0:
            book_author_output_line = f'{author_ids[author_name]}\t{isbn}\n'
            book_author_output.append(book_author_output_line)
        else:
            book_author_output_line = f'\t{isbn}\n'
            book_author_output.append(book_author_output_line)

auth.writelines(author_output)
book.writelines(book_output)
book_author_file.writelines(book_author_output)