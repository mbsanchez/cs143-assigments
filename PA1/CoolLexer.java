/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025; 
    int nestedComment = 0;
    // For assembling string constants
    StringBuffer string = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return yyline+1;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int NULLINSTRING = 3;
	private final int yy_state_dtrans[] = {
		0,
		79,
		103,
		108
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NOT_ACCEPT,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"68,24:7,63,64,26,27,65,25,24:13,66,24:4,27,24,36,24:4,67,20,22,21,40,33,23," +
"34,41,59:10,32,31,37,38,60,24,39,42,43,44,45,46,7,43,47,48,43:2,49,43,50,51" +
",52,43,53,54,12,55,13,56,43:3,24,61,24:2,57,24,3,62,1,15,5,6,58,10,8,58:2,2" +
",58,9,14,16,58,11,4,18,19,28,17,58:3,29,69,30,35,24,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,190,
"0,1,2,3,4,5,1,6,1,7,1:8,8,9,1:3,10,11:2,12,11,1:2,13,1:3,11:7,14,11:7,1:11," +
"15,1:8,16,1:9,17,18,19,20,1,14:2,21,14:8,11,14:5,22,23,24,25,26,11,27,28,29" +
",30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54" +
",55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79" +
",80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,11,14,99,100,101," +
"102,103,104,105,106,107")[0];

	private int yy_nxt[][] = unpackFromString(108,70,
"1,2,139,179:2,181,80,3,104,141,179:2,81,180,109,179,183,185,187,179,4,5,6,7" +
",8,9,83:2,106,10,11,12,13,14,15,16,17,18,19,20,21,22,180:2,182,180,184,180," +
"105,140,142,110,186,180:3,188,8,179,23,8:2,179,8,83:2,8:4,-1:71,179,189,143" +
",179:16,-1:8,179,-1:13,143,179:6,189,179:10,-1:2,179,-1:8,180:7,84,180:11,-" +
"1:8,180,-1:13,180:6,84,180:11,-1:2,180,-1:28,28,-1:70,29,-1:70,30,-1:72,83," +
"-1:66,31,-1:14,32,-1:91,33,-1:68,23,-1:11,179:19,-1:8,179,-1:13,179:18,-1:2" +
",179,-1:8,179:9,169,179:9,-1:8,179,-1:13,179:5,169,179:12,-1:2,179,-1:8,30:" +
"24,-1:2,30:42,-1:2,180:19,-1:8,180,-1:13,180:18,-1:2,180,-1:74,67,-1:24,71," +
"-1:47,1,49:24,50,51,49:9,52,49:24,82,49:6,53,49,-1,179:2,151,179:4,24,179:1" +
"1,-1:8,179,-1:13,151,179:5,24,179:11,-1:2,179,-1:8,180:9,144,180:9,-1:8,180" +
",-1:13,180:5,144,180:12,-1:2,180,-1:8,54:5,55,54:2,56,54:8,57,54,-1:3,54,-1" +
":2,58,-1,54,-1:7,59,-1:3,54,-1,54:15,-1,54:2,-1,60,61,62,63,64,65,-1,66,-1:" +
"2,180:9,166,180:9,-1:8,180,-1:13,180:5,166,180:12,-1:2,180,-1:28,70,-1:74,7" +
"8,-1:43,1,68:19,101,69,68:39,107,68:8,-1,179:3,153,179,25:2,179,26,179:10,-" +
"1:8,179,-1:13,179:8,26,179:3,153,179:5,-1:2,179,-1:8,180:3,154,180,85:2,180" +
",86,180:10,-1:8,180,-1:13,180:8,86,180:3,154,180:5,-1:2,180,-1:27,72,73,74," +
"-1:47,1,75:25,76,75:9,77,75:24,102,75:8,-1,179:5,27:2,179:12,-1:8,179,-1:13" +
",179:18,-1:2,179,-1:8,180:5,87:2,180:12,-1:8,180,-1:13,180:18,-1:2,180,-1:8" +
",179:11,34,179:5,34,179,-1:8,179,-1:13,179:18,-1:2,179,-1:8,180:11,88,180:5" +
",88,180,-1:8,180,-1:13,180:18,-1:2,180,-1:8,179:16,35,179:2,-1:8,179,-1:13," +
"179:14,35,179:3,-1:2,179,-1:8,180:16,89,180:2,-1:8,180,-1:13,180:14,89,180:" +
"3,-1:2,180,-1:8,179:11,36,179:5,36,179,-1:8,179,-1:13,179:18,-1:2,179,-1:8," +
"180:11,90,180:5,90,180,-1:8,180,-1:13,180:18,-1:2,180,-1:8,179:4,37,179:14," +
"-1:8,179,-1:13,179:4,37,179:13,-1:2,179,-1:8,180:8,41,180:10,-1:8,180,-1:13" +
",180:8,41,180:9,-1:2,180,-1:8,179:15,38,179:3,-1:8,179,-1:13,179:10,38,179:" +
"7,-1:2,179,-1:8,180:4,91,180:14,-1:8,180,-1:13,180:4,91,180:13,-1:2,180,-1:" +
"8,179:4,39,179:14,-1:8,179,-1:13,179:4,39,179:13,-1:2,179,-1:8,180:4,93,180" +
":14,-1:8,180,-1:13,180:4,93,180:13,-1:2,180,-1:8,40,179:18,-1:8,179,-1:13,1" +
"79:2,40,179:15,-1:2,179,-1:8,94,180:18,-1:8,180,-1:13,180:2,94,180:15,-1:2," +
"180,-1:8,179,42,179:17,-1:8,179,-1:13,179:7,42,179:10,-1:2,179,-1:8,180:15," +
"92,180:3,-1:8,180,-1:13,180:10,92,180:7,-1:2,180,-1:8,179:8,95,179:10,-1:8," +
"179,-1:13,179:8,95,179:9,-1:2,179,-1:8,180,96,180:17,-1:8,180,-1:13,180:7,9" +
"6,180:10,-1:2,180,-1:8,179:4,43,179:14,-1:8,179,-1:13,179:4,43,179:13,-1:2," +
"179,-1:8,180:3,97,180:15,-1:8,180,-1:13,180:12,97,180:5,-1:2,180,-1:8,179:3" +
",44,179:15,-1:8,179,-1:13,179:12,44,179:5,-1:2,179,-1:8,180:4,98,180:14,-1:" +
"8,180,-1:13,180:4,98,180:13,-1:2,180,-1:8,179:4,45,179:14,-1:8,179,-1:13,17" +
"9:4,45,179:13,-1:2,179,-1:8,180:14,99,180:4,-1:8,180,-1:13,180:3,99,180:14," +
"-1:2,180,-1:8,179:4,46,179:14,-1:8,179,-1:13,179:4,46,179:13,-1:2,179,-1:8," +
"180:3,100,180:15,-1:8,180,-1:13,180:12,100,180:5,-1:2,180,-1:8,179:14,47,17" +
"9:4,-1:8,179,-1:13,179:3,47,179:14,-1:2,179,-1:8,179:3,48,179:15,-1:8,179,-" +
"1:13,179:12,48,179:5,-1:2,179,-1:8,179:4,111,179:8,145,179:5,-1:8,179,-1:13" +
",179:4,111,179:4,145,179:8,-1:2,179,-1:8,180:4,112,180:8,156,180:5,-1:8,180" +
",-1:13,180:4,112,180:4,156,180:8,-1:2,180,-1:8,179:4,113,179:8,115,179:5,-1" +
":8,179,-1:13,179:4,113,179:4,115,179:8,-1:2,179,-1:8,180:4,114,180:8,116,18" +
"0:5,-1:8,180,-1:13,180:4,114,180:4,116,180:8,-1:2,180,-1:8,179:3,117,179:15" +
",-1:8,179,-1:13,179:12,117,179:5,-1:2,179,-1:8,180:4,118,180:14,-1:8,180,-1" +
":13,180:4,118,180:13,-1:2,180,-1:8,179:13,119,179:5,-1:8,179,-1:13,179:9,11" +
"9,179:8,-1:2,179,-1:8,180:2,162,180:16,-1:8,180,-1:13,162,180:17,-1:2,180,-" +
"1:8,179:3,121,179:15,-1:8,179,-1:13,179:12,121,179:5,-1:2,179,-1:8,180:3,12" +
"0,180:15,-1:8,180,-1:13,180:12,120,180:5,-1:2,180,-1:8,179:2,123,179:16,-1:" +
"8,179,-1:13,123,179:17,-1:2,179,-1:8,180:3,122,180:15,-1:8,180,-1:13,180:12" +
",122,180:5,-1:2,180,-1:8,179,165,179:17,-1:8,179,-1:13,179:7,165,179:10,-1:" +
"2,179,-1:8,180:2,124,180:16,-1:8,180,-1:13,124,180:17,-1:2,180,-1:8,179:12," +
"167,179:6,-1:8,167,-1:13,179:18,-1:2,179,-1:8,180:12,164,180:6,-1:8,164,-1:" +
"13,180:18,-1:2,180,-1:8,179:13,125,179:5,-1:8,179,-1:13,179:9,125,179:8,-1:" +
"2,179,-1:8,180:13,126,180:5,-1:8,180,-1:13,180:9,126,180:8,-1:2,180,-1:8,17" +
"9:7,171,179:11,-1:8,179,-1:13,179:6,171,179:11,-1:2,179,-1:8,180:13,128,180" +
":5,-1:8,180,-1:13,180:9,128,180:8,-1:2,180,-1:8,179:4,127,179:14,-1:8,179,-" +
"1:13,179:4,127,179:13,-1:2,179,-1:8,180:7,168,180:11,-1:8,180,-1:13,180:6,1" +
"68,180:11,-1:2,180,-1:8,179:18,129,-1:8,179,-1:13,179:13,129,179:4,-1:2,179" +
",-1:8,180:3,130,180:15,-1:8,180,-1:13,180:12,130,180:5,-1:2,180,-1:8,179:3," +
"131,179:15,-1:8,179,-1:13,179:12,131,179:5,-1:2,179,-1:8,180:13,170,180:5,-" +
"1:8,180,-1:13,180:9,170,180:8,-1:2,180,-1:8,179:3,133,179:15,-1:8,179,-1:13" +
",179:12,133,179:5,-1:2,179,-1:8,180:4,172,180:14,-1:8,180,-1:13,180:4,172,1" +
"80:13,-1:2,180,-1:8,179:13,173,179:5,-1:8,179,-1:13,179:9,173,179:8,-1:2,17" +
"9,-1:8,180,132,180:17,-1:8,180,-1:13,180:7,132,180:10,-1:2,180,-1:8,179:4,1" +
"75,179:14,-1:8,179,-1:13,179:4,175,179:13,-1:2,179,-1:8,180:7,134,180:11,-1" +
":8,180,-1:13,180:6,134,180:11,-1:2,180,-1:8,179,135,179:17,-1:8,179,-1:13,1" +
"79:7,135,179:10,-1:2,179,-1:8,180:10,174,180:8,-1:8,180,-1:13,180:11,174,18" +
"0:6,-1:2,180,-1:8,179:7,137,179:11,-1:8,179,-1:13,179:6,137,179:11,-1:2,179" +
",-1:8,180:7,176,180:11,-1:8,180,-1:13,180:6,176,180:11,-1:2,180,-1:8,179:10" +
",177,179:8,-1:8,179,-1:13,179:11,177,179:6,-1:2,179,-1:8,180:11,136,180:5,1" +
"36,180,-1:8,180,-1:13,180:18,-1:2,180,-1:8,179:7,178,179:11,-1:8,179,-1:13," +
"179:6,178,179:11,-1:2,179,-1:8,179:11,138,179:5,138,179,-1:8,179,-1:13,179:" +
"18,-1:2,179,-1:8,179,147,179,149,179:15,-1:8,179,-1:13,179:7,147,179:4,149," +
"179:5,-1:2,179,-1:8,180,146,148,180:16,-1:8,180,-1:13,148,180:6,146,180:10," +
"-1:2,180,-1:8,179:13,155,179:5,-1:8,179,-1:13,179:9,155,179:8,-1:2,179,-1:8" +
",180,150,180,152,180:15,-1:8,180,-1:13,180:7,150,180:4,152,180:5,-1:2,180,-" +
"1:8,179:9,157,179:9,-1:8,179,-1:13,179:5,157,179:12,-1:2,179,-1:8,180:13,15" +
"8,180:5,-1:8,180,-1:13,180:9,158,180:8,-1:2,180,-1:8,179:9,159,161,179:8,-1" +
":8,179,-1:13,179:5,159,179:5,161,179:6,-1:2,179,-1:8,180:9,160,180:9,-1:8,1" +
"80,-1:13,180:5,160,180:12,-1:2,180,-1:8,179:2,163,179:16,-1:8,179,-1:13,163" +
",179:17,-1:2,179,-1:7");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    case COMMENT:
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "EOF in string");
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.MULT); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.MINUS); }
					case -8:
						break;
					case 8:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
				return new Symbol(TokenConstants.ERROR, yytext());
                                }
					case -9:
						break;
					case 9:
						{ /* ignore */ }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.SEMI); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.COLON); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.COMMA); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.DOT); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.NEG); }
					case -17:
						break;
					case 17:
						{ string.setLength(0); yybegin(STRING); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.LT); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.EQ); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.AT); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.PLUS); }
					case -22:
						break;
					case 22:
						{ return new Symbol(TokenConstants.DIV); }
					case -23:
						break;
					case 23:
						{ return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.FI); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.IF); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.IN); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.OF); }
					case -28:
						break;
					case 28:
						{ nestedComment = 1; yybegin(COMMENT); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
					case -30:
						break;
					case 30:
						{ /* ignore */ }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.LE); }
					case -33:
						break;
					case 33:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LET); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.NEW); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NOT); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.CASE); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.LOOP); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.ELSE); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ESAC); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.THEN); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.POOL); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));  }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.CLASS); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.WHILE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -49:
						break;
					case 49:
						{ string.append( yytext() ); }
					case -50:
						break;
					case 50:
						{ string.append( '\r' ); }
					case -51:
						break;
					case 51:
						{ yybegin(YYINITIAL);
				  return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); }
					case -52:
						break;
					case 52:
						{ yybegin(YYINITIAL); 
				  if(string.length()>=MAX_STR_CONST)
					return new Symbol(TokenConstants.ERROR, "String constant too long");
			          return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string.toString(), string.length())); }
					case -53:
						break;
					case 53:
						{ yybegin(NULLINSTRING); }
					case -54:
						break;
					case 54:
						{ string.append( yytext().charAt(1) ); }
					case -55:
						break;
					case 55:
						{ string.append( '\f' ); }
					case -56:
						break;
					case 56:
						{ string.append( '\n' ); }
					case -57:
						break;
					case 57:
						{ string.append( '\t' ); }
					case -58:
						break;
					case 58:
						{ string.append( '\n' ); }
					case -59:
						break;
					case 59:
						{ string.append( '\"' ); }
					case -60:
						break;
					case 60:
						{ string.append( '\\' ); }
					case -61:
						break;
					case 61:
						{ string.append( '\b' ); }
					case -62:
						break;
					case 62:
						{ string.append( '\b' ); }
					case -63:
						break;
					case 63:
						{ string.append( '\t' ); }
					case -64:
						break;
					case 64:
						{ string.append( '\f' ); }
					case -65:
						break;
					case 65:
						{ string.append( '\033' ); }
					case -66:
						break;
					case 66:
						{ yybegin(NULLINSTRING); }
					case -67:
						break;
					case 67:
						{ string.append( '\'' ); }
					case -68:
						break;
					case 68:
						{ /*ignore*/ }
					case -69:
						break;
					case 69:
						{ /*ignore*/ }
					case -70:
						break;
					case 70:
						{ nestedComment++; }
					case -71:
						break;
					case 71:
						{ nestedComment--; 
				  if(nestedComment==0) yybegin(YYINITIAL); }
					case -72:
						break;
					case 72:
						{ /*ignore*/ }
					case -73:
						break;
					case 73:
						{ /*ignore*/ }
					case -74:
						break;
					case 74:
						{ /*ignore*/ }
					case -75:
						break;
					case 75:
						{  }
					case -76:
						break;
					case 76:
						{ yybegin(YYINITIAL); 
				  return new Symbol(TokenConstants.ERROR, "String contains null character"); }
					case -77:
						break;
					case 77:
						{ yybegin(YYINITIAL);
				  return new Symbol(TokenConstants.ERROR, "String contains null character"); }
					case -78:
						break;
					case 78:
						{  }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -81:
						break;
					case 82:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
				return new Symbol(TokenConstants.ERROR, yytext());
                                }
					case -82:
						break;
					case 83:
						{ /* ignore */ }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.FI); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.IF); }
					case -85:
						break;
					case 86:
						{ return new Symbol(TokenConstants.IN); }
					case -86:
						break;
					case 87:
						{ return new Symbol(TokenConstants.OF); }
					case -87:
						break;
					case 88:
						{ return new Symbol(TokenConstants.LET); }
					case -88:
						break;
					case 89:
						{ return new Symbol(TokenConstants.NEW); }
					case -89:
						break;
					case 90:
						{ return new Symbol(TokenConstants.NOT); }
					case -90:
						break;
					case 91:
						{ return new Symbol(TokenConstants.CASE); }
					case -91:
						break;
					case 92:
						{ return new Symbol(TokenConstants.LOOP); }
					case -92:
						break;
					case 93:
						{ return new Symbol(TokenConstants.ELSE); }
					case -93:
						break;
					case 94:
						{ return new Symbol(TokenConstants.ESAC); }
					case -94:
						break;
					case 95:
						{ return new Symbol(TokenConstants.THEN); }
					case -95:
						break;
					case 96:
						{ return new Symbol(TokenConstants.POOL); }
					case -96:
						break;
					case 97:
						{ return new Symbol(TokenConstants.CLASS); }
					case -97:
						break;
					case 98:
						{ return new Symbol(TokenConstants.WHILE); }
					case -98:
						break;
					case 99:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -99:
						break;
					case 100:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -100:
						break;
					case 101:
						{ /*ignore*/ }
					case -101:
						break;
					case 102:
						{  }
					case -102:
						break;
					case 104:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -103:
						break;
					case 105:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -104:
						break;
					case 106:
						{ /* ignore */ }
					case -105:
						break;
					case 107:
						{ /*ignore*/ }
					case -106:
						break;
					case 109:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -107:
						break;
					case 110:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -108:
						break;
					case 111:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -109:
						break;
					case 112:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -110:
						break;
					case 113:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -111:
						break;
					case 114:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -112:
						break;
					case 115:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -113:
						break;
					case 116:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -114:
						break;
					case 117:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -115:
						break;
					case 118:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -116:
						break;
					case 119:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -117:
						break;
					case 120:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -118:
						break;
					case 121:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -119:
						break;
					case 122:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -120:
						break;
					case 123:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -121:
						break;
					case 124:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -122:
						break;
					case 125:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -123:
						break;
					case 126:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -124:
						break;
					case 127:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -125:
						break;
					case 128:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -126:
						break;
					case 129:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -127:
						break;
					case 130:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -128:
						break;
					case 131:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -129:
						break;
					case 132:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -130:
						break;
					case 133:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -131:
						break;
					case 134:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -132:
						break;
					case 135:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -133:
						break;
					case 136:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -134:
						break;
					case 137:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -135:
						break;
					case 138:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -136:
						break;
					case 139:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -137:
						break;
					case 140:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -138:
						break;
					case 141:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -139:
						break;
					case 142:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -140:
						break;
					case 143:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -141:
						break;
					case 144:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -142:
						break;
					case 145:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -143:
						break;
					case 146:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -144:
						break;
					case 147:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -145:
						break;
					case 148:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -146:
						break;
					case 149:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -147:
						break;
					case 150:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -148:
						break;
					case 151:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -149:
						break;
					case 152:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -150:
						break;
					case 153:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -151:
						break;
					case 154:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -152:
						break;
					case 155:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -153:
						break;
					case 156:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -154:
						break;
					case 157:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -155:
						break;
					case 158:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -156:
						break;
					case 159:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -157:
						break;
					case 160:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -158:
						break;
					case 161:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -159:
						break;
					case 162:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -160:
						break;
					case 163:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -161:
						break;
					case 164:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -162:
						break;
					case 165:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -163:
						break;
					case 166:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -164:
						break;
					case 167:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -165:
						break;
					case 168:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -166:
						break;
					case 169:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -167:
						break;
					case 170:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -168:
						break;
					case 171:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -169:
						break;
					case 172:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -170:
						break;
					case 173:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -171:
						break;
					case 174:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -172:
						break;
					case 175:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -173:
						break;
					case 176:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -174:
						break;
					case 177:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -175:
						break;
					case 178:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -176:
						break;
					case 179:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -177:
						break;
					case 180:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -178:
						break;
					case 181:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -179:
						break;
					case 182:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -180:
						break;
					case 183:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -181:
						break;
					case 184:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -182:
						break;
					case 185:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -183:
						break;
					case 186:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -184:
						break;
					case 187:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -185:
						break;
					case 188:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -186:
						break;
					case 189:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -187:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
