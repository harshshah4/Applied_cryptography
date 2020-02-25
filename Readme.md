## Applied Cryptography problem statenents and solution code

#### MIMA-2DES

Conecpt : 2 DES and Meet in the middle attack
Given : 2DES_k1,k2(x)=DES(DES(x,k1),k2), where k1 and k2 are two random keys of length 56-bit each and x is the input plaintext of 64-bit.
Aim: Develop a computer program which takes <x1,y1> and <x2,y2> as the input and produces k1 and k2 as outputs.
Assumption: For simplicity and scalability reasons, I have assumed that the first 32-bits of k1 and k2 are the same.

#### AES-CTR-MERKEL

Conecpt : AES mode encryption scheme, CTR block cipher, Merkel puzzle logic
Given : A list of encrypted message in text file "encrypted_merkel_puzzle_format.txt" using the same AES-128 key (in the ctr mode). The encrypted message inside each puzzle has this format:”Puzzle is  x:k” (without quotes), where both x and k are  128-bit random numbers.
Aim: Find the key(k=?) in the puzzle text file that corresponds to x (=740AC607E4F3A32193DA750FACF38D87).
Assumption: For simplicity and scalability reasons, the first 14 bytes of the key for AES decryption are just zero. So we need to find the other two bytes of the key that was used to encrypt all the puzzles.

#### 
